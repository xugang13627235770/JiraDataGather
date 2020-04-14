/**
 * Copyright 2019 bejson.com
 */
package com.citms.modules.jira.vo.project;

import com.citms.modules.jira.vo.issue.User;
import lombok.Data;

/**
 * Auto-generated: 2019-11-11 10:53:33
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Data
public class Project {

    private String self;
    private String id;
    private String key;
    private String name;
    private String projectTypeKey;
    private User   projectLead;
}