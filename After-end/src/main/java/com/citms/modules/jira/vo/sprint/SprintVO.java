/**
 * Copyright 2019 bejson.com
 */
package com.citms.modules.jira.vo.sprint;

/**
 * Auto-generated: 2019-11-11 11:2:7
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class SprintVO {

    private long   timestamp;
    private String webhookEvent;
    private Sprint sprint;

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

    public void setSprint(Sprint sprint) {
        this.sprint = sprint;
    }

    public Sprint getSprint() {
        return sprint;
    }

}