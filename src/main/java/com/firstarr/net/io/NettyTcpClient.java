package com.firstarr.net.io;

import com.firstarr.net.base.ChannelGroup;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NettyTcpClient extends NettyNetwork{

    private EventLoopGroup group;
    private Bootstrap clientBootstrap;
    protected Map<Channel,String> tcpClientMap;

    public NettyTcpClient(ChannelGroup channelGroup, NettyChannelGroup nettyChannelGroup) {
        super(channelGroup,nettyChannelGroup);
        this.group = new NioEventLoopGroup();
        this.clientBootstrap = new Bootstrap();
        this.tcpClientMap = new ConcurrentHashMap<>();
    }

    public void init(){
        clientBootstrap.group(group);
        clientBootstrap.channel(NioSocketChannel.class);
        clientBootstrap.handler(new ChannelInitializer<SocketChannel>() {
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline()
                        .addLast("byteArrayEncoder",new ByteArrayEncoder())
                        .addLast("byteArrayDecoder",new TcpClientHandler(NettyTcpClient.this));
            }
        });
    }

    public Channel connect(String host, int port, String code) throws InterruptedException {
        ChannelFuture channelFuture = clientBootstrap.connect(host,port).sync();
        Channel channel = channelFuture.channel();
        tcpClientMap.put(channel,code);
        return channel;
    }

}
