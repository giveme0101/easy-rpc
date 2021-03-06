package com.github.easyrpc.core.provider.client;

import io.netty.channel.Channel;

import java.net.InetSocketAddress;

public interface ChannelProvider {

    Channel getChannel(InetSocketAddress inetSocketAddress);

    void removeChannel(InetSocketAddress inetSocketAddress);

    void cleanChannel();

}
