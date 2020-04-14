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
public class Votes {

    private String  self;
    private int     votes;
    private boolean hasVoted;

    public void setSelf(String self) {
        this.self = self;
    }

    public String getSelf() {
        return self;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public int getVotes() {
        return votes;
    }

    public void setHasVoted(boolean hasVoted) {
        this.hasVoted = hasVoted;
    }

    public boolean getHasVoted() {
        return hasVoted;
    }

}