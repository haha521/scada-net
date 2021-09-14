package com.firstarr.net.base;

public interface IChannel {

    void connect() throws InterruptedException;

    void close() throws InterruptedException;

    void write(byte[] bytes);

}
