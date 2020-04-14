/**
 * Copyright 2019 bejson.com
 */
package com.citms.modules.jira.vo.issue;

import com.citms.modules.jira.vo.project.Project;
import lombok.Data;

import java.util.List;

/**
 * Auto-generated: 2019-11-11 10:53:33
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Data
public class Fields {

    private Issuetype        issuetype;
    private List<Components> components;
    private String           timespent;
    private String           timeoriginalestimate;
    private String           description;
    private Project          project;
    private String           aggregatetimespent;
    private Resolution       resolution;
    private String           customfield_10104;
    private Customfield      customfield_10203;
    private String           customfield_10105;
    private String           customfield_10204;
    private String           customfield_10106;
    private List<Customfield>    customfield_10205;
    private List<Attachment>     attachment;
    private String           aggregatetimeestimate;
    private String           resolutiondate;
    private int              workratio;
    private String            summary;
    private String            lastViewed;
    private Watches           watches;
    private User              creator;
    private List<String>      subtasks;
    private String            created;
    private User          reporter;
    private String            customfield_10000;
    private Aggregateprogress aggregateprogress;
    private Priority          priority;
    private String            customfield_10100;
    private Customfield       customfield_10201;
    private List<String>      labels;
    private String            environment;
    private String            timeestimate;
    private String            aggregatetimeoriginalestimate;
    private String            duedate;
    private Progress          progress;
    private Comment           comment;
    private List<String>      issuelinks;
    private Votes             votes;
    private Worklog           worklog;
    private User              assignee;
    private String              updated;
    private Status            status;
}