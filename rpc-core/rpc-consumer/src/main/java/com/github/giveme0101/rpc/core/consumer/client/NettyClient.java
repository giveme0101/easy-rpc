package com.github.giveme0101.rpc.core.consumer.client;

import com.github.giveme0101.rpc.core.common.codec.RpcDecoder;
import com.github.giveme0101.rpc.core.common.codec.RpcEncoder;
import com.github.giveme0101.rpc.core.common.entity.RpcRequest;
import com.github.giveme0101.rpc.core.common.entity.RpcResponse;
import com.github.giveme0101.rpc.core.common.serialize.Serializer;
import com.github.giveme0101.rpc.core.common.serialize.protostuff.ProtoStuffSerializer;
import com.github.giveme0101.rpc.core.consumer.handle.ChannelClientHandler;
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
import java.util.concurrent.CompletableFuture;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name Client
 * @Date 2020/09/14 10:35
 */
@Slf4j
public class NettyClient {

    private EventLoopGroup bossGroup = new NioEventLoopGroup(1);

    private Bootstrap client;

    @Setter
    private Serializer serializer;

    public NettyClient() {
        if (null == serializer){
            serializer = new ProtoStuffSerializer();
        }

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

    @SneakyThrows
    public Channel connect(InetSocketAddress socketAddress){

        CompletableFuture<Channel> future = new CompletableFuture();

        client.connect(socketAddress).addListener((ChannelFutureListener) listener -> {
            if (listener.isSuccess()) {
                log.info("netty client has connected [{}]", socketAddress.toString());
                future.complete(listener.channel());
            } else {
                throw new IllegalStateException();
            }
        });

       return future.get();
    }

    public void stop(){
        bossGroup.shutdownGracefully();
    }

}