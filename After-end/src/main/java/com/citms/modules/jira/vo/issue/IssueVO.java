/**
 * Copyright 2019 bejson.com
 */
package com.citms.modules.jira.vo.issue;

/**
 * Auto-generated: 2019-11-11 10:53:33
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class IssueVO {

    private long   timestamp;
    private String webhookEvent;
    private String issue_event_type_name;
    private User   user;
    private Issue  issue;
    private Changelog changelog;

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setWebhookEvent(String webhookEvent) {
        this.webhookEvent = webhookEvent;
    }

    public String getWebhookEvent() {
        return webhookEvent;
    }

    public void setIssue_event_type_name(String issue_event_type_name) {
        this.issue_event_type_name = issue_event_type_name;
    }

    public String getIssue_event_type_name() {
        return issue_event_type_name;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    public Issue getIssue() {
        return issue;
    }

    public Changelog getChangelog() {
        return changelog;
    }

    public void setChangelog(Changelog changelog) {
        this.changelog = changelog;
    }
}