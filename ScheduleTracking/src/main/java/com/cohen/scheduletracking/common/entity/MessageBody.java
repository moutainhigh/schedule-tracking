package com.cohen.scheduletracking.common.entity;

public class MessageBody {

    private String status;// 响应状态
    private String body;// 响应内容

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
