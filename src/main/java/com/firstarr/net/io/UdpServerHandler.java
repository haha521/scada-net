package com.firstarr.net.io;

import com.firstarr.net.base.AbstractChannel;
import com.firstarr.net.base.ConnectType;
import com.firstarr.net.base.IChannel;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

import java.net.InetSocketAddress;
import java.util.ArrayList;

public class UdpServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    private NettyUdpServer nettyUdpServer;

    public UdpServerHandler(NettyUdpServer nettyUdpServer) {
        this.nettyUdpServer = nettyUdpServer;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket) throws Exception {
        InetSocketAddress sender = datagramPacket.sender();
        Channel channel = channelHandlerContext.channel();
        ByteBuf content = datagramPacket.content();
        int beginReader = content.readerIndex();
        if(content.readShort() == Package.FUNC1 && content.readShort() == Package.DISTINGUISH){
            int length = content.readShort();
            byte[] bytes = new byte[length];
            content.readBytes(bytes);
            String code = ConvertUtils.convertHexToString(ConvertUtils.BinaryToHexString(bytes));
            IChannel iChannel = nettyUdpServer.getMyChannel(code);
            AbstractChannel abstractChannel = (AbstractChannel) iChannel;
            if(abstractChannel != null){
                if(abstractChannel.getConnectType() == ConnectType.UDP_SERVER){
                    NettyChannel nettyChannel = new NettyChannel();
                    nettyChannel.setChannel(channel);
                    nettyChannel.setHost(sender.getHostName());
                    nettyChannel.setPort(sender.getPort());
                    nettyChannel.setConnectType(ConnectType.UDP_SERVER);
                    if(nettyUdpServer.addChannel(code,nettyChannel)){
                        nettyUdpServer.onMessage(MessageType.CONNECT,code,sender.getPort(),sender.getHostName(),null,null);
                        System.out.println("客户端："+code+":认证完成！");
                    }else {
                        NettyChannel myChannel = nettyUdpServer.getChannel(code);
                        myChannel.setPort(sender.getPort());
                        myChannel.setHost(sender.getHostName());
                    }
                }
            }
        }else {
            content.readerIndex(beginReader);
            if(content.readShort() == Package.FUNC2 && content.readShort() == Package.DISTINGUISH1){
                int length = content.readShort();
                byte[] bytes = new byte[length];
                content.readBytes(bytes);
                String code = ConvertUtils.convertHexToString(ConvertUtils.BinaryToHexString(bytes));
                NettyChannel nettyChannel = nettyUdpServer.getChannel(code);
                if(nettyChannel != null && nettyChannel.getConnectType() == ConnectType.UDP_SERVER){
                    nettyUdpServer.onMessage(MessageType.READ,code,sender.getPort(),sender.getHostName(),content,new ArrayList<>());
                }
            }else {
                int length = content.readableBytes();
                byte[] bytes = new byte[length];
                content.readBytes(bytes);
            }
        }
    }


}
