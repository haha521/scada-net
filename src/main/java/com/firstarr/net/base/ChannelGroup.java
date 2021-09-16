package com.firstarr.net.base;

import com.firstarr.net.io.MyNettyException;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChannelGroup {

    private final Map<String, AbstractChannel> channelMap;

    public ChannelGroup() {
        channelMap = new ConcurrentHashMap<>();
    }

    public synchronized void addChannel(String code, AbstractChannel channel){
        if(!channelMap.containsKey(code)){
            channelMap.put(code,channel);
        }else {
            AbstractChannel abstractChannel = channelMap.get(code);
            if(abstractChannel.isState() && (channel.getConnectType() == ConnectType.TCP_CLIENT || channel.getConnectType() == ConnectType.UDP_CLIENT)){
                try {
                    abstractChannel.close();
                } catch (InterruptedException | MyNettyException e) {
                    e.printStackTrace();
                }
                long start = new Date().getTime();
                while (abstractChannel.isState()){
                    if(new Date().getTime() - start >= 10000 || !abstractChannel.isState()){
                        abstractChannel.setActive(false);
                        channelMap.replace(code,channel);
                        break;
                    }
                }
            }else {
                abstractChannel.setActive(false);
                channelMap.replace(code,channel);
            }
        }
    }

    public AbstractChannel getChannel(String code){
        return channelMap.get(code);
    }

    public void removeChannel(String code){
        channelMap.remove(code);
    }

    public void onMessage(Result result){
        AbstractChannel abstractChannel = getChannel(result.getCode());
        if(abstractChannel != null){
            abstractChannel.setIpAddr(result.getHost());
            abstractChannel.setPort(result.getPort());
            IChannelListener channelListener = abstractChannel.getChannelListener();
            if(channelListener!=null){
                switch (result.getMessageType()){
                    case READ:
                        channelListener.onRead(abstractChannel, result.getByteBuf());
                        break;
                    case CONNECT:
                        abstractChannel.setState(true);
                        channelListener.onConnect(abstractChannel);
                        break;
                    case CLOSE:
                        abstractChannel.setState(false);
                        channelListener.onClose(abstractChannel);
                        break;
                }
            }
        }
    }

}
