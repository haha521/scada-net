package com.firstarr.net.io;

import com.firstarr.net.base.ChannelGroup;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class NettyUdpServer extends NettyNetwork{

    private int port;

    public NettyUdpServer(int port, ChannelGroup channelGroup, NettyChannelGroup nettyChannelGroup) {
        super(channelGroup,nettyChannelGroup);
        this.port = port;
    }

    public void run() throws InterruptedException {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup).channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST, true)
                .handler(new ChannelInitializer<NioDatagramChannel>() { // (4)
                    @Override
                    public void initChannel(NioDatagramChannel ch) {
                        ch.pipeline().addLast("device auth",new UdpServerHandler(NettyUdpServer.this));
                    }
                });

        ChannelFuture channelFuture = bootstrap.bind(port).sync();
        System.out.println("Server start listen at " + port );
        channelFuture.channel().closeFuture().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                eventLoopGroup.shutdownGracefully();
                System.out.println("Server closed");
            }
        });
    }

}
