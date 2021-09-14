package com.firstarr.net.io;

import com.firstarr.net.base.ChannelGroup;
import com.firstarr.net.base.IChannel;
import com.firstarr.net.base.Result;
import io.netty.buffer.ByteBuf;

public class NettyNetwork {

    private ChannelGroup channelGroup;
    private NettyChannelGroup nettyChannelGroup;

    public NettyNetwork(ChannelGroup channelGroup, NettyChannelGroup nettyChannelGroup) {
        this.channelGroup = channelGroup;
        this.nettyChannelGroup = nettyChannelGroup;
    }

    public boolean addChannel(String code, NettyChannel channel){
        return nettyChannelGroup.addChannel(code,channel);
    }

    public void removeChannel(String code){
        nettyChannelGroup.removeChannel(code);
    }

    public NettyChannel getChannel(String code){
        return nettyChannelGroup.getChannel(code);
    }

    public void onMessage(MessageType messageType, String code, int port, String host, ByteBuf byteBuf){
        Result result = new Result(messageType,code,port,host,byteBuf);
        channelGroup.onMessage(result);
    }

    public IChannel getMyChannel(String code){
        return channelGroup.getChannel(code);
    }

}
