package com.github.giveme0101.rpc.core.client.util;

import io.netty.channel.Channel;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name SocketChannelHolder
 * @Date 2020/09/14 14:32
 */
public class SocketChannelHolder {

    private static Channel CHANNEL = null;

    public static void set(Channel channel){
        CHANNEL = channel;
    }

    public static Channel get(){
        return CHANNEL;
    }

}
