/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.citms.modules.sys.sevice.impl;

import com.citms.modules.sys.dao.SysUserDao;
import com.citms.modules.sys.dao.SysUserTokenDao;
import com.citms.modules.sys.entity.SysUserEntity;
import com.citms.modules.sys.entity.SysUserTokenEntity;
import com.citms.modules.sys.sevice.ShiroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class ShiroServiceImpl implements ShiroService {
//    @Autowired
//    private SysMenuDao      sysMenuDao;
    @Autowired
    private SysUserDao      sysUserDao;
    @Autowired
    private SysUserTokenDao sysUserTokenDao;

    @Override
    public Set<String> getUserPermissions(long userId) {
//        List<String> permsList;
//
//        //系统管理员，拥有最高权限
//        if(userId == Constant.SUPER_ADMIN){
//            List<SysMenuEntity> menuList = sysMenuDao.selectList(null);
//            permsList = new ArrayList<>(menuList.size());
//            for(SysMenuEntity menu : menuList){
//                permsList.add(menu.getPerms());
//            }
//        }else{
//            permsList = sysUserDao.queryAllPerms(userId);
//        }
//        //用户权限列表
//        Set<String> permsSet = new HashSet<>();
//        for(String perms : permsList){
//            if(StringUtils.isBlank(perms)){
//                continue;
//            }
//            permsSet.addAll(Arrays.asList(perms.trim().split(",")));
//        }
        return new HashSet<>();
    }

    @Override
    public SysUserTokenEntity queryByToken(String token) {
        return sysUserTokenDao.queryByToken(token);
    }

    @Override
    public SysUserEntity queryUser(Long userId) {
        return sysUserDao.selectById(userId);
    }
}
