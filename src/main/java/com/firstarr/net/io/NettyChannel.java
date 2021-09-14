package com.firstarr.net.io;

import com.firstarr.net.base.ConnectType;
import io.netty.channel.Channel;

public class NettyChannel {

    private Channel channel;

    private int port;

    private String host;

    private ConnectType connectType;

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public ConnectType getConnectType() {
        return connectType;
    }

    public void setConnectType(ConnectType connectType) {
        this.connectType = connectType;
    }
}
