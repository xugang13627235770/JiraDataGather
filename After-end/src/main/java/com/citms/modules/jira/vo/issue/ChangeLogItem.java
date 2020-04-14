package com.citms.modules.jira.vo.issue;

/**
 * 类Items的实现描述：TODO 类实现描述 
 * @author DELL 2019/11/11 11:23
 */
public class ChangeLogItem {
    private String field;

    private String fieldtype;

    private String from;

    private String fromString;

    private String to;

    private String toString;

    public void setField(String field){
        this.field = field;
    }
    public String getField(){
        return this.field;
    }
    public void setFieldtype(String fieldtype){
        this.fieldtype = fieldtype;
    }
    public String getFieldtype(){
        return this.fieldtype;
    }
    public void setFrom(String from){
        this.from = from;
    }
    public String getFrom(){
        return this.from;
    }
    public void setFromString(String fromString){
        this.fromString = fromString;
    }
    public String getFromString(){
        return this.fromString;
    }
    public void setTo(String to){
        this.to = to;
    }
    public String getTo(){
        return this.to;
    }
    public void setToString(String toString){
        this.toString = toString;
    }
    public String getToString(){
        return this.toString;
    }

}
