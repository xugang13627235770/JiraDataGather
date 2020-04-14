/**
 * Copyright 2019 bejson.com
 */
package com.citms.modules.jira.vo.sprint;

import lombok.Data;

/**
 * Auto-generated: 2019-11-11 11:2:7
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Data
public class Sprint {

    private int    id;
    private String self;
    private String state;
    private String   name;
    private String   startDate;
    private String   endDate;
    private int    originBoardId;
    private String goal;

}