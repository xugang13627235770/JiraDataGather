package com.citms.modules.jira.service;

import java.lang.reflect.InvocationTargetException;

/**

 * @since 2018/03
 * @version R1.0
 * @category 变更日志表
 */
public interface JiraDataService {

    /**
     * 将jira推送过来的数据存储到数据库
     * @param json
     */
    void saveJiraData(String json) throws InvocationTargetException, IllegalAccessException;

}