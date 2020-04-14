package com.citms.modules.home.service;

import java.util.Map;

/**

 * @since 2018/03
 * @version R1.0
 */
public interface HomeService{

    /**
     * 获取首页问题统计
     * @return
     */
    Map<String, Object> getHomeIssue(Long projectId);
}