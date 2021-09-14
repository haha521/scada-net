package com.firstarr.net.io;

import com.firstarr.net.base.ChannelGroup;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class NettyTcpServer extends NettyNetwork{

    private int port;
    protected Map<Channel,TcpCode> tcpCodeMap;
    protected ScheduledExecutorService executorService;

    public NettyTcpServer(int port, ChannelGroup channelGroup, NettyChannelGroup nettyChannelGroup) {
        super(channelGroup,nettyChannelGroup);
        this.port = port;
        this.tcpCodeMap = new ConcurrentHashMap<>();
        this.executorService = Executors.newScheduledThreadPool(10);
    }

    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap(); // (2)
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class) // (3)
                .childHandler(new ChannelInitializer<SocketChannel>() { // (4)
                    @Override
                    public void initChannel(SocketChannel ch) {
                        ch.pipeline()
                                .addLast("byteArrayEncoder",new ByteArrayEncoder())
                                .addLast("byteArrayDecoder",new TcpServerHandler(NettyTcpServer.this));
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128)          // (5)
                .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

        // 绑定端口，开始接收进来的连接
        ChannelFuture channelFuture = b.bind(port).sync(); // (7)
        System.out.println("Server start listen at " + port );
        channelFuture.channel().closeFuture().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
                System.out.println("Server closed");
            }
        });

    }
}
