package com.github.easyrpc.core.consumer.client;

import com.github.easyrpc.common.context.IContext;
import com.github.easyrpc.common.codec.RpcDecoder;
import com.github.easyrpc.common.codec.RpcEncoder;
import com.github.easyrpc.common.entity.RpcRequest;
import com.github.easyrpc.common.entity.RpcResponse;
import com.github.easyrpc.common.serialize.Serializer;
import com.github.easyrpc.common.server.IServer;
import com.github.easyrpc.common.util.Assert;
import com.github.easyrpc.core.consumer.handle.ChannelClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name Client
 * @Date 2020/09/14 10:35
 */
@Slf4j
public class NettyClient implements IServer, ChannelProvider {

    private Bootstrap client;
    private IContext context;
    private EventLoopGroup bossGroup;
    private final Map<String, Channel> channelMap;

    @Setter
    private Serializer serializer;

    public NettyClient(IContext context) {
        this.context = context;
        this.channelMap = new HashMap<>();
        this.bossGroup = new NioEventLoopGroup(1);
    }

    @Override
    public void start() {
        Assert.notNull(serializer, "netty serializer is null");
        client = new Bootstrap()
                .group(bossGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {

                        nioSocketChannel.pipeline()

                                // 添加心跳 30秒没有向server发送消息，触发
                                .addLast(new IdleStateHandler(0, 30, 0))

                                .addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 0))

                                .addLast(new RpcEncoder(RpcRequest.class, serializer))
                                .addLast(new RpcDecoder(RpcResponse.class, serializer))

                                .addLast(new ChannelClientHandler());
                    }
                });
    }

    @Override
    public void stop(){
        this.cleanChannel();
        bossGroup.shutdownGracefully();
    }

    @SneakyThrows
    private Channel connect(InetSocketAddress socketAddress){

        CompletableFuture<Channel> future = new CompletableFuture();

        client.connect(socketAddress).addListener((ChannelFutureListener) listener -> {
            if (listener.isSuccess()) {
                log.debug("netty client connected: {}", socketAddress);
                future.complete(listener.channel());
            } else {
                throw new IllegalStateException();
            }
        });

        return future.get();
    }

    @Override
    public Channel getChannel(InetSocketAddress inetSocketAddress) {

        String key = inetSocketAddress.toString();
        if (channelMap.containsKey(key)) {
            Channel channel = channelMap.get(key);
            if (channel != null && channel.isActive()) {
                return channel;
            }
        }

        Channel channel = this.connect(inetSocketAddress);
        log.debug("connect server: {}", inetSocketAddress);
        channelMap.put(key, channel);
        return channel;
    }

    @Override
    public void removeChannel(InetSocketAddress inetSocketAddress) {
        log.info("remove channel: {}", inetSocketAddress);
        String key = inetSocketAddress.toString();
        Channel channel = channelMap.remove(key);
        if (channel.isOpen()) {
            channel.close();
        }
        log.debug("Channel map size :[{}]", channelMap.size());
    }

    @Override
    public void cleanChannel() {
        log.debug("clean channel");
        for (final Map.Entry<String, Channel> entry : channelMap.entrySet()) {
            Channel channel = entry.getValue();
            if (channel.isOpen()){
                channel.close();
            }
        }
        channelMap.clear();
    }

}