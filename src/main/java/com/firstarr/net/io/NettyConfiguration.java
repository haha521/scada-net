package com.firstarr.net.io;

import com.firstarr.net.base.ChannelGroup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NettyConfiguration {

    private final static NettyChannelGroup NETTY_CHANNEL_GROUP = new NettyChannelGroup();
    private final static ChannelGroup CHANNEL_GROUP = new ChannelGroup();

    @Value("${tcp.port:9090}")
    private int tcpPort;

    @Value("${udp.port:9091}")
    private int udpPort;

    @Bean
    public ChannelBuilder getChannelBuilder() throws Exception {
        NettyUdpServer nettyUdpServer = new NettyUdpServer(udpPort,CHANNEL_GROUP,NETTY_CHANNEL_GROUP);
        nettyUdpServer.run();
        NettyTcpServer nettyTcpServer = new NettyTcpServer(tcpPort,CHANNEL_GROUP,NETTY_CHANNEL_GROUP);
        nettyTcpServer.run();
        NettyTcpClient nettyTcpClient = new NettyTcpClient(CHANNEL_GROUP,NETTY_CHANNEL_GROUP);
        nettyTcpClient.init();
        NettyUdpClient nettyUdpClient = new NettyUdpClient(CHANNEL_GROUP,NETTY_CHANNEL_GROUP);
        nettyUdpClient.init();
        return new ChannelBuilder(nettyTcpClient,nettyUdpClient,nettyTcpServer,nettyUdpServer,NETTY_CHANNEL_GROUP,CHANNEL_GROUP);
    }

}
