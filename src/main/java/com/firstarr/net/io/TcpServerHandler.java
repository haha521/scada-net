package com.firstarr.net.io;

import com.firstarr.net.base.AbstractChannel;
import com.firstarr.net.base.ConnectType;
import com.firstarr.net.base.IChannel;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TcpServerHandler extends ByteToMessageDecoder {

    private NettyTcpServer nettyTcpServer;

    public TcpServerHandler(NettyTcpServer nettyTcpServer) {
        this.nettyTcpServer = nettyTcpServer;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        Channel channel = ctx.channel();
        InetSocketAddress inetSocketAddress = (InetSocketAddress)channel.remoteAddress();
        TcpCode tcpCode = nettyTcpServer.tcpCodeMap.get(channel);
        if(tcpCode.getCode() != null){
            nettyTcpServer.removeChannel(tcpCode.getCode());
            nettyTcpServer.onMessage(MessageType.CLOSE,tcpCode.getCode(),inetSocketAddress.getPort(),inetSocketAddress.getHostName(),null,null);
        }
        nettyTcpServer.tcpCodeMap.remove(channel);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        TcpCode tcpCode = new TcpCode();
        Channel channel = ctx.channel();
        nettyTcpServer.tcpCodeMap.put(ctx.channel(),tcpCode);
        nettyTcpServer.executorService.schedule(new Runnable() {
            @Override
            public void run() {
                if(tcpCode.isFirst()){
                    channel.close();
                }
            }
        },10, TimeUnit.SECONDS);
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        Channel channel = channelHandlerContext.channel();
        InetSocketAddress inetSocketAddress = (InetSocketAddress)channel.remoteAddress();
        TcpCode tcpCode = nettyTcpServer.tcpCodeMap.get(channel);
        if(tcpCode.isFirst()){
            // 记录包头开始的index
            int beginReader;
            boolean isRead = false;

            int num = byteBuf.readableBytes();
            if(num < 4){
                return;
            }else {
                // 获取包头开始的index
                beginReader = byteBuf.readerIndex();
                // 读到了协议的开始标志，结束while循环
                if (byteBuf.readShort() == Package.FUNC1 && byteBuf.readShort() == Package.DISTINGUISH) {
                    isRead = true;
                }
            }

            if(isRead){
                // 消息的长度
                int length = byteBuf.readShort();
                // 判断请求数据包数据是否到齐
                if (byteBuf.readableBytes() < length) {
                    // 还原读指针
                    byteBuf.readerIndex(beginReader);
                    return;
                }

                // 读取data数据
                byte[] data = new byte[length];
                byteBuf.readBytes(data);
                String code = new String(data, "UTF-8");

                NettyChannel nettyChannel = new NettyChannel();
                nettyChannel.setPort(inetSocketAddress.getPort());
                nettyChannel.setHost(inetSocketAddress.getHostName());
                nettyChannel.setChannel(channel);
                nettyChannel.setConnectType(ConnectType.TCP_SERVER);
                IChannel iChannel = nettyTcpServer.getMyChannel(code);
                AbstractChannel abstractChannel = (AbstractChannel) iChannel;
                if(abstractChannel != null && abstractChannel.getConnectType() == ConnectType.TCP_SERVER && nettyTcpServer.addChannel(code,nettyChannel)){
                    System.out.println("客户端："+code+":认证完成！");
                    tcpCode.setCode(code);
                    tcpCode.setFirst(false);
                    nettyTcpServer.onMessage(MessageType.CONNECT,code,nettyChannel.getPort(),nettyChannel.getHost(),null,null);
                }else {
                    if(tcpCode.isOpen()){
                        channelHandlerContext.channel().close();
                        tcpCode.setOpen(false);
                    }
                }
            }else {
                if(tcpCode.isOpen()){
                    channelHandlerContext.channel().close();
                    tcpCode.setOpen(false);
                }
            }
        }else {
            NettyChannel nettyChannel = nettyTcpServer.getChannel(tcpCode.getCode());
            if(nettyChannel != null && nettyChannel.getConnectType() == ConnectType.TCP_SERVER){
                nettyTcpServer.onMessage(MessageType.READ,tcpCode.getCode(),inetSocketAddress.getPort(),inetSocketAddress.getHostName(),byteBuf,list);
            }
        }
    }

}
