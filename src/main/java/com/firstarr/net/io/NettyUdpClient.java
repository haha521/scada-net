package com.firstarr.net.io;

import com.firstarr.net.base.ChannelGroup;
import com.firstarr.net.base.ConnectType;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class NettyUdpClient extends NettyNetwork{

    private EventLoopGroup group;
    private Bootstrap clientBootstrap;
    protected Map<Channel,String> udpClientMap;

    public NettyUdpClient(ChannelGroup channelGroup, NettyChannelGroup nettyChannelGroup) {
        super(channelGroup,nettyChannelGroup);
        this.group = new NioEventLoopGroup();
        this.clientBootstrap = new Bootstrap();
        this.udpClientMap = new ConcurrentHashMap<>();
    }

    public void init(){
        clientBootstrap.group(group).channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST,true)
                .handler(new ChannelInitializer<NioDatagramChannel>() { // (4)
                    @Override
                    public void initChannel(NioDatagramChannel ch) {
                        ch.pipeline().addLast("device auth",new UdpClientHandler(NettyUdpClient.this));
                    }
                });
    }

    public Channel bind(String code) throws InterruptedException {
        ChannelFuture channelFuture = clientBootstrap.bind(0).sync();
        Channel channel = channelFuture.channel();
        udpClientMap.put(channel,code);
        return channel;
    }

}
