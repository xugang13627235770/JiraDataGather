package com.citms.modules.user.vo;

import com.citms.modules.user.entity.UserEntity;
import lombok.Data;

/**
 * 类UserVO的实现描述：TODO 类实现描述 
 * @author DELL 2019/12/2 9:57
 */
@Data
public class UserVO extends UserEntity {

    //用户类型值
    private String userTypeStr;

    //累计故事
    private Long totalStory;

    //累计缺陷
    private Long totalDefect;
}
