package com.citms.modules.jira.vo.issue;

/**
 * 类Attachment的实现描述：TODO 类实现描述 
 * @author DELL 2019/11/13 11:24
 */
/**
 * Copyright 2019 bejson.com
 */

import lombok.Data;

import java.util.Date;
@Data
public class Attachment {
    private String self;
    private String id;
    private String filename;
    private Date   created;
    private long   size;
    private String mimeType;
    private String content;
    private String thumbnail;

}
