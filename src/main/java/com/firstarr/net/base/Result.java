package com.firstarr.net.base;


import com.firstarr.net.io.MessageType;
import io.netty.buffer.ByteBuf;

import java.util.List;

public class Result {

    private MessageType messageType;

    private String code;

    private int port;

    private String host;

    private ByteBuf byteBuf;

    private List<Object> out;

    public Result() {
    }

    public Result(MessageType messageType, String code, int port, String host, ByteBuf byteBuf,List<Object> out) {
        this.messageType = messageType;
        this.code = code;
        this.port = port;
        this.host = host;
        this.byteBuf = byteBuf;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public ByteBuf getByteBuf() {
        return byteBuf;
    }

    public void setByteBuf(ByteBuf byteBuf) {
        this.byteBuf = byteBuf;
    }

    public List<Object> getOut() {
        return out;
    }

    public void setOut(List<Object> out) {
        this.out = out;
    }
}
