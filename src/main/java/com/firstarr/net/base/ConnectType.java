package com.firstarr.net.base;

public enum ConnectType {

    TCP_SERVER(1,"tcp服务器"),
    TCP_CLIENT(2,"tcp客户端"),
    UDP_SERVER(3,"udp服务器"),
    UDP_CLIENT(4,"udp客户端");

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

    ConnectType(Integer type, String description) {
        this.description = description;
        this.type = type;
    }

}
