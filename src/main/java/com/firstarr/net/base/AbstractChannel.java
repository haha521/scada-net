package com.firstarr.net.base;

public abstract class AbstractChannel implements IChannel{

    protected String code;

    protected ConnectType connectType;

    protected int port;

    protected String ipAddr; //ipAddr

    protected IChannelListener channelListener;

    protected volatile boolean state;

    protected volatile boolean active = true;

    public AbstractChannel(String code, ConnectType connectType, int port, String ipAddr, IChannelListener channelListener) {
        this.code = code;
        this.connectType = connectType;
        this.port = port;
        this.ipAddr = ipAddr;
        this.channelListener = channelListener;
    }

    protected IChannelListener getChannelListener() {
        return channelListener;
    }

    public String getCode() {
        return code;
    }

    protected void setCode(String code) {
        this.code = code;
    }

    public ConnectType getConnectType() {
        return connectType;
    }

    protected void setConnectType(ConnectType connectType) {
        this.connectType = connectType;
    }

    public int getPort() {
        return port;
    }

    protected void setPort(int port) {
        this.port = port;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    protected void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public boolean isState() {
        return state;
    }

    protected void setState(boolean state) {
        this.state = state;
    }

    public boolean isActive() {
        return active;
    }

    protected void setActive(boolean active) {
        this.active = active;
    }
}
