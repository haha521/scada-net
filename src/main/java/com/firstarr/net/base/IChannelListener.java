package com.firstarr.net.base;

import io.netty.buffer.ByteBuf;

public interface IChannelListener {

    void onConnect(AbstractChannel channel);

    void onClose(AbstractChannel channel);

    void onRead(AbstractChannel channel, ByteBuf byteBuf);

}
