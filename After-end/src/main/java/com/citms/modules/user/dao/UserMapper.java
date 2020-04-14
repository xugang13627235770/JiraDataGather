package com.citms.modules.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.citms.modules.user.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @since 2018/03
 * @version R1.0
 * @category 变更日志表
 */
@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {

    /**
     * 根据key，查询value
     */
    UserEntity queryUserByName(String name);

    /**
     * 查询用户信息
     * @param iPage
     * @param key
     * @param userType
     * @return
     */
    List<UserEntity> getUserListByCon(IPage<UserEntity> iPage, @Param("key") String key, @Param("userType") String userType);

    /**
     * 查询用户信息数量
     * @param key
     * @param userType
     * @return
     */
    Integer getUserListCount(@Param("key") String key, @Param("userType") String userType);

    /**
     * 批量修改用户类型
     * @return
     */
    Integer updateUserTypeByIds(@Param("ids") List<String> ids, @Param("userType") String userType);

}