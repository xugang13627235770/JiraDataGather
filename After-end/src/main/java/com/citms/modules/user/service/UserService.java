package com.citms.modules.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.citms.common.utils.PageUtils;
import com.citms.modules.user.entity.UserEntity;

import java.util.List;

/**

 * @since 2018/03
 * @version R1.0
 * @category 变更日志表
 */
public interface UserService extends IService<UserEntity> {

    /**
     * 查询用户列表
     * @param iPage
     * @param key
     * @param userType
     * @return
     */
    PageUtils getUserList(IPage<UserEntity> iPage, String key, String userType);

    /**
     * 批量修改用户类型
     * @param ids
     * @param userType
     */
    void updateUserTypeByIds(List<String> ids,  String userType);
}