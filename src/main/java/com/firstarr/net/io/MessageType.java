package com.firstarr.net.io;

public enum MessageType {

    CONNECT(1,"连接消息"),
    CLOSE(2,"关闭消息"),
    READ(3,"读消息");

    private Integer type;
    private String description;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    MessageType(Integer type, String description) {
        this.description = description;
        this.type = type;
    }

}
