package com.firstarr.net.io;

import com.firstarr.net.base.*;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.socket.DatagramPacket;

import java.net.InetSocketAddress;

public class ChannelFactory {

    private NettyNetwork nettyNetwork;
    private NettyChannelGroup nettyChannelGroup;
    private ChannelGroup channelGroup;

    public ChannelFactory(NettyNetwork nettyNetwork, NettyChannelGroup nettyChannelGroup, ChannelGroup channelGroup) {
        this.nettyNetwork = nettyNetwork;
        this.nettyChannelGroup = nettyChannelGroup;
        this.channelGroup = channelGroup;
    }

    public AbstractChannel getInstance(String code, String host, int port, ConnectType connectType, IChannelListener channelListener){
        AbstractChannel channel = new AbstractChannel(code,connectType,port,host,channelListener) {
            @Override
            public void connect() throws InterruptedException, MyNettyException {
                if(!active){
                    throw new MyNettyException("通道已被重置！");
                }
                if(state){
                    throw new MyNettyException("通道已连接！");
                }
                NettyChannel nettyChannel = nettyChannelGroup.getChannel(code);
                if(nettyChannel != null){
                    if(connectType == ConnectType.TCP_CLIENT){
                        NettyTcpClient nettyTcpClient = (NettyTcpClient) nettyNetwork;
                        nettyChannel.getChannel().close().sync();
                        nettyChannelGroup.removeChannel(code);
                        Channel myChannel = nettyTcpClient.connect(host,port,code);
                        NettyChannel myNettyChannel = new NettyChannel();
                        myNettyChannel.setChannel(myChannel);
                        myNettyChannel.setHost(host);
                        myNettyChannel.setPort(port);
                        myNettyChannel.setConnectType(connectType);
                        nettyChannelGroup.addChannel(code,myNettyChannel);
                    }
                    if(connectType == ConnectType.UDP_CLIENT){
                        NettyUdpClient nettyUdpClient = (NettyUdpClient) nettyNetwork;
                        nettyChannel.getChannel().close().sync();
                        nettyChannelGroup.removeChannel(code);
                        Channel myChannel = nettyUdpClient.bind(code);
                        NettyChannel myNettyChannel = new NettyChannel();
                        myNettyChannel.setChannel(myChannel);
                        myNettyChannel.setHost(host);
                        myNettyChannel.setPort(port);
                        myNettyChannel.setConnectType(connectType);
                        nettyChannelGroup.addChannel(code,myNettyChannel);
                    }
                    state = true;
                    Result result = new Result(MessageType.CONNECT,code,port,host,null,null);
                    channelGroup.onMessage(result);
                }else {
                    if(connectType == ConnectType.TCP_CLIENT){
                        NettyTcpClient nettyTcpClient = (NettyTcpClient) nettyNetwork;
                        Channel myChannel = nettyTcpClient.connect(host,port,code);
                        NettyChannel myNettyChannel = new NettyChannel();
                        myNettyChannel.setChannel(myChannel);
                        myNettyChannel.setHost(host);
                        myNettyChannel.setPort(port);
                        myNettyChannel.setConnectType(connectType);
                        nettyChannelGroup.addChannel(code,myNettyChannel);
                        state = true;
                        Result result = new Result(MessageType.CONNECT,code,port,host,null,null);
                        channelGroup.onMessage(result);
                    }else if(connectType == ConnectType.UDP_CLIENT){
                        NettyUdpClient nettyUdpClient = (NettyUdpClient) nettyNetwork;
                        Channel myChannel = nettyUdpClient.bind(code);
                        NettyChannel myNettyChannel = new NettyChannel();
                        myNettyChannel.setChannel(myChannel);
                        myNettyChannel.setHost(host);
                        myNettyChannel.setPort(port);
                        myNettyChannel.setConnectType(connectType);
                        nettyChannelGroup.addChannel(code,myNettyChannel);
                        state = true;
                        Result result = new Result(MessageType.CONNECT,code,port,host,null,null);
                        channelGroup.onMessage(result);
                    }
                }
            }

            @Override
            public void close() throws InterruptedException, MyNettyException {
                if(!active){
                    throw new MyNettyException("通道已被重置！");
                }
                if(!state){
                    throw new MyNettyException("通道未连接！");
                }
                NettyChannel nettyChannel = nettyChannelGroup.getChannel(code);
                if(nettyChannel != null){
                    if(connectType == ConnectType.TCP_SERVER || connectType == ConnectType.TCP_CLIENT){
                        nettyChannel.getChannel().close().sync();
                    }else if(connectType == ConnectType.UDP_CLIENT){
                        nettyChannel.getChannel().close().sync();
                        nettyChannelGroup.removeChannel(code);
                        state = false;
                        Result result = new Result(MessageType.CLOSE,code,port,host,null,null);
                        channelGroup.onMessage(result);
                    }else {
                        nettyChannelGroup.removeChannel(code);
                        state = false;
                        Result result = new Result(MessageType.CLOSE,code,port,host,null,null);
                        channelGroup.onMessage(result);
                    }
                }
            }

            @Override
            public void write(byte[] bytes) throws MyNettyException {
                if(!active){
                    throw new MyNettyException("通道已被重置！");
                }
                if(!state){
                    throw new MyNettyException("通道未连接！");
                }
                NettyChannel nettyChannel = nettyChannelGroup.getChannel(code);
                if(nettyChannel != null){
                    if(connectType == ConnectType.TCP_CLIENT || connectType == ConnectType.TCP_SERVER){
                        nettyChannel.getChannel().writeAndFlush(bytes);
                    }
                    if(connectType == ConnectType.UDP_CLIENT || connectType == ConnectType.UDP_SERVER){
                        InetSocketAddress address = new InetSocketAddress(nettyChannel.getHost(), nettyChannel.getPort());
                        ByteBuf byteBuf = Unpooled.copiedBuffer(bytes);
                        nettyChannel.getChannel().writeAndFlush(new DatagramPacket(byteBuf, address));
                    }
                }
            }
        };
        channelGroup.addChannel(code,channel);
        return channel;
    }

}
