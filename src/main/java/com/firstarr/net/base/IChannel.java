package com.firstarr.net.base;

import com.firstarr.net.io.MyNettyException;

public interface IChannel {

    void connect() throws InterruptedException, MyNettyException;

    void close() throws InterruptedException, MyNettyException;

    void write(byte[] bytes) throws MyNettyException;

}
