/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.citms.modules.sys.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.citms.common.utils.R;
import com.citms.modules.sprintproject.entity.SprintEntity;
import com.citms.modules.sprintproject.entity.SprintProjectEntity;
import com.citms.modules.developer.service.DeveloperHistoryService;
import com.citms.modules.sprintproject.service.SprintProjectService;
import com.citms.modules.sprintproject.service.SprintService;
import com.citms.modules.tester.service.TesterHistoryService;
import com.citms.modules.sys.entity.SysUserEntity;
import com.citms.modules.sys.entity.SysUserTokenEntity;
import com.citms.modules.sys.form.SysLoginForm;
import com.citms.modules.sys.sevice.ShiroService;
import com.citms.modules.sys.sevice.SysUserService;
import com.citms.modules.sys.sevice.SysUserTokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 登录相关
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@Api(description ="登录相关接口")
public class SysLoginController extends AbstractController {
	@Autowired
	private SysUserService      sysUserService;
	@Autowired
	private SysUserTokenService sysUserTokenService;
	@Autowired
	private ShiroService        shiroService;

	/**
	 * 登录
	 */
	@ApiOperation("登录")
	@PostMapping("/sys/login")
	public R login(SysLoginForm form){

		//用户信息
		SysUserEntity user = sysUserService.queryByUserName(form.getUserName());

		//账号不存在、密码错误
		if(user == null || !user.getPassword().equals(new Sha256Hash(form.getPassword(), user.getSalt()).toHex())) {
			return R.error("账号或密码不正确");
		}

		//账号锁定
		if(user.getStatus() == 0){
			return R.error("账号已被锁定,请联系管理员");
		}

		//生成token，并保存到数据库
		R r = sysUserTokenService.createToken(user.getUserId());
		return r;
	}


	/**
	 * 退出
	 */
	@ApiOperation("退出")
	@PostMapping("/sys/logout")
	public R logout(String token) {
		//根据accessToken，查询用户信息
		SysUserTokenEntity tokenEntity = shiroService.queryByToken(token);
		sysUserTokenService.logout(tokenEntity.getUserId());
		return R.ok();
	}

	@ApiOperation("注册")
	@PostMapping("/sys/register")
	public R register(String loginName, String password){
		if(StringUtils.isEmpty(loginName)){
			return R.error("账号不能为空");
		}
		//用户信息
		SysUserEntity user = sysUserService.queryByUserName(loginName);
		Long userId = null;
		if(user != null){
			userId = user.getUserId();
			user.setUsername(loginName);
			user.setPassword(password);
			user.setStatus(1);
			sysUserService.update(user);
		}else{
			SysUserEntity sysUserEntity = new SysUserEntity();
			sysUserEntity.setUsername(loginName);
			sysUserEntity.setPassword(password);
			sysUserEntity.setStatus(1);
			sysUserService.saveUser(sysUserEntity);
			userId = sysUserEntity.getUserId();
		}

		//生成token，并保存到数据库
		R r = sysUserTokenService.createToken(userId);
		return r;
	}

	@Autowired
	private SprintProjectService sprintProjectService;
	@Autowired
	private DeveloperHistoryService developerHistoryService;
	@Autowired
	private TesterHistoryService testerHistoryService;
	@Autowired
	private SprintService sprintService;
	@ApiOperation("初始化开发测试统计历史记录")
	@PostMapping("/history/init")
	public R init() {
		List<SprintProjectEntity> sprintProjectList = sprintProjectService.getAllSprintProjectList();
		if(!CollectionUtils.isEmpty(sprintProjectList)){
			for(SprintProjectEntity sprintProjectEntity : sprintProjectList){
				developerHistoryService.updateNextSprintId(sprintProjectEntity.getSprintId());
				testerHistoryService.updateNextSprintId(sprintProjectEntity.getSprintId());

				SprintEntity sprintCon = new SprintEntity();
				sprintCon.setId(sprintProjectEntity.getSprintId());
				Wrapper<SprintEntity> ew = Wrappers.query(sprintCon);
				SprintEntity sprintEntity = sprintService.getBaseMapper().selectOne(ew);
				if(new Integer(1).equals(sprintEntity.getState())){
					continue;
				}

				developerHistoryService.saveDeveloperHistory(sprintProjectEntity.getSprintId());
				testerHistoryService.saveTesterHistory(sprintProjectEntity.getSprintId());
			}
		}
		return R.ok();
	}
	
}
