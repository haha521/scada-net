package com.firstarr.net.io;

import com.firstarr.net.base.ConnectType;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class TcpClientHandler extends ByteToMessageDecoder {

    private NettyTcpClient nettyTcpClient;

    public TcpClientHandler(NettyTcpClient nettyTcpClient) {
        this.nettyTcpClient = nettyTcpClient;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        Channel channel = ctx.channel();
        String code = nettyTcpClient.tcpClientMap.get(channel);
        if(code != null){
            NettyChannel nettyChannel = nettyTcpClient.getChannel(code);
            if(nettyChannel != null){
                nettyTcpClient.removeChannel(code);
                nettyTcpClient.onMessage(MessageType.CLOSE,code,nettyChannel.getPort(),nettyChannel.getHost(),null);
            }
        }
        nettyTcpClient.tcpClientMap.remove(ctx.channel());
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        String code = nettyTcpClient.tcpClientMap.get(ctx.channel());
        NettyChannel nettyChannel = nettyTcpClient.getChannel(code);
        if(nettyChannel != null && nettyChannel.getConnectType() == ConnectType.TCP_CLIENT){
            nettyTcpClient.onMessage(MessageType.READ,code,nettyChannel.getPort(),nettyChannel.getHost(),in);
        }
    }

}
