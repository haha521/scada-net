package com.firstarr.net.io;

import com.firstarr.net.base.*;

public class ChannelBuilder {

    private NettyTcpClient nettyTcpClient;
    private NettyUdpClient nettyUdpClient;
    private NettyTcpServer nettyTcpServer;
    private NettyUdpServer nettyUdpServer;
    private NettyChannelGroup nettyChannelGroup;
    private ChannelGroup channelGroup;

    public ChannelBuilder(NettyTcpClient nettyTcpClient, NettyUdpClient nettyUdpClient, NettyTcpServer nettyTcpServer, NettyUdpServer nettyUdpServer, NettyChannelGroup nettyChannelGroup, ChannelGroup channelGroup) {
        this.nettyTcpClient = nettyTcpClient;
        this.nettyUdpClient = nettyUdpClient;
        this.nettyTcpServer = nettyTcpServer;
        this.nettyUdpServer = nettyUdpServer;
        this.nettyChannelGroup = nettyChannelGroup;
        this.channelGroup = channelGroup;
    }

    public AbstractChannel newChannel(String code, String host, int port, ConnectType connectType, IChannelListener channelListener){
        NettyNetwork nettyNetwork = null;
        if(connectType == ConnectType.TCP_CLIENT){
            nettyNetwork = nettyTcpClient;
        }else if(connectType == ConnectType.TCP_SERVER){
            nettyNetwork = nettyTcpServer;
        }else if(connectType == ConnectType.UDP_CLIENT){
            nettyNetwork = nettyUdpClient;
        }else if(connectType == ConnectType.UDP_SERVER){
            nettyNetwork = nettyUdpServer;
        }
        ChannelFactory factory = new ChannelFactory(nettyNetwork,nettyChannelGroup,channelGroup);
        return factory.getInstance(code,host,port,connectType,channelListener);
    }
}
