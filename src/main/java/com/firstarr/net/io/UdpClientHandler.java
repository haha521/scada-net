package com.firstarr.net.io;

import com.firstarr.net.base.ConnectType;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

public class UdpClientHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    private NettyUdpClient nettyUdpClient;

    public UdpClientHandler(NettyUdpClient nettyUdpClient) {
        this.nettyUdpClient = nettyUdpClient;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        nettyUdpClient.udpClientMap.remove(ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
        ByteBuf byteBuf = msg.content();
        Channel channel = ctx.channel();
        String code = nettyUdpClient.udpClientMap.get(channel);
        NettyChannel nettyChannel = nettyUdpClient.getChannel(code);
        if(nettyChannel != null && nettyChannel.getConnectType() == ConnectType.UDP_CLIENT){
            nettyUdpClient.onMessage(MessageType.READ,code,nettyChannel.getPort(),nettyChannel.getHost(),byteBuf);
        }
    }

}
