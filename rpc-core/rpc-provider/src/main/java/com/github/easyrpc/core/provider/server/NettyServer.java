package com.github.easyrpc.core.provider.server;

import com.github.easyrpc.common.context.IContext;
import com.github.easyrpc.common.codec.RpcDecoder;
import com.github.easyrpc.common.codec.RpcEncoder;
import com.github.easyrpc.common.entity.ContextEvent;
import com.github.easyrpc.common.entity.RpcRequest;
import com.github.easyrpc.common.entity.RpcResponse;
import com.github.easyrpc.common.serialize.Serializer;
import com.github.easyrpc.common.server.IServer;
import com.github.easyrpc.common.util.Assert;
import com.github.easyrpc.common.util.NetUtil;
import com.github.easyrpc.core.provider.handle.ChannelServerHandler;
import com.github.easyrpc.core.provider.util.LocalAddressHolder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name Server
 * @Date 2020/09/14 13:57
 */
@Slf4j
public class NettyServer implements IServer, Runnable {

    private int port = 23500;
    private long start;
    private IContext context;
    private AtomicInteger status = new AtomicInteger(STOP);
    private EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private EventLoopGroup workGroup = new NioEventLoopGroup(4);
    private ChannelFuture channelFuture;

    @Setter
    private Serializer serializer;
    @Setter
    private ServiceProvider serviceProvider;

    private static final int STOP = 0;
    private static final int RUNNING = 1;

    public NettyServer(IContext context){
        start = System.currentTimeMillis();
        this.context = context;
        while (NetUtil.isPortUsing(port)){
            port++;
        }
    }

    @Override
    public void run() {
        this.start();
    }

    @Override
    public void start(){
        Assert.notNull(serializer, "netty serializer is null");
        if (status.compareAndSet(STOP, RUNNING)) {
            start0();
        }
    }

    @Override
    public void stop(){
        if (status.compareAndSet(RUNNING, STOP)) {
            Channel channel = channelFuture.channel();
            if (channel.isOpen()) {
                channel.close();
            }
        }
    }

    private void start0(){

        try {
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)

                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel channel) throws Exception {

                            channel.pipeline()

                                    // 心跳检测，60秒没有收到客户端的消息，触发
                                    .addLast(new IdleStateHandler(60, 0, 0))

                                    .addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 0))

                                    .addLast(new RpcDecoder(RpcRequest.class, serializer))
                                    .addLast(new RpcEncoder(RpcResponse.class, serializer))

                                    .addLast(new ChannelServerHandler(serviceProvider));
                        }
                    });

            LocalAddressHolder.HOST = NetUtil.getHost();
            LocalAddressHolder.PORT = port;

            channelFuture = bootstrap.bind(port).sync();
            if (channelFuture.isSuccess()) {
                context.publish(ContextEvent.SERVER_STARTING);
                log.info("starting netty server in {}ms, port: {}", System.currentTimeMillis() - start, port);
            }
            channelFuture.channel().closeFuture().sync();
        } catch (Exception ex){
            log.error(ex.getMessage(), ex);
        } finally {
            log.info("shutting down netty event group...");
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

}