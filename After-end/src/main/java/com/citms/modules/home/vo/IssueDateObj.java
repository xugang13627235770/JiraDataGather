package com.citms.modules.home.vo;

import lombok.Data;

import java.util.List;

/**
 * 类IssueDateObj的实现描述：TODO 类实现描述 
 * @author DELL 2019/12/3 17:24
 */
@Data
public class IssueDateObj {
    private Integer          issueStatus;//问题状态
    private String           type;//问题状态值
    private List<Integer> data;//问题数量
}
