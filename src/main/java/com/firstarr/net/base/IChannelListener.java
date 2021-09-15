package com.firstarr.net.base;

import io.netty.buffer.ByteBuf;

import java.util.List;

public interface IChannelListener {

    void onConnect(AbstractChannel channel);

    void onClose(AbstractChannel channel);

    void onRead(AbstractChannel channel, ByteBuf byteBuf, List<Object> out);

}
