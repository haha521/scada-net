package com.firstarr.net.io;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NettyChannelGroup {

    private Map<String, NettyChannel> channelMap;

    public NettyChannelGroup() {
        channelMap = new ConcurrentHashMap<>();
    }

    public synchronized boolean addChannel(String code,NettyChannel channel){
        if(!channelMap.containsKey(code)){
            channelMap.put(code,channel);
            return true;
        }
        return false;
    }

    public void removeChannel(String code){
        channelMap.remove(code);
    }

    public NettyChannel getChannel(String code){
        return channelMap.get(code);
    }

}
