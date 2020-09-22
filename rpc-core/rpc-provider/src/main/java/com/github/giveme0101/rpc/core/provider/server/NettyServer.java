package com.github.giveme0101.rpc.core.provider.server;

import com.github.giveme0101.rpc.core.common.codec.RpcDecoder;
import com.github.giveme0101.rpc.core.common.codec.RpcEncoder;
import com.github.giveme0101.rpc.core.common.entity.RpcRequest;
import com.github.giveme0101.rpc.core.common.entity.RpcResponse;
import com.github.giveme0101.rpc.core.common.serialize.Serializer;
import com.github.giveme0101.rpc.core.common.serialize.protostuff.ProtoStuffSerializer;
import com.github.giveme0101.rpc.core.common.util.NetUtil;
import com.github.giveme0101.rpc.core.provider.context.AbstractContext;
import com.github.giveme0101.rpc.core.provider.handle.ChannelServerHandler;
import com.github.giveme0101.rpc.core.provider.util.LocalAddressHolder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name Server
 * @Date 2020/09/14 13:57
 */
@Slf4j
public class NettyServer implements Runnable {

    private EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private EventLoopGroup workGroup = new NioEventLoopGroup(4);

    private int port = 23500;
    private long start;
    private AtomicInteger status = new AtomicInteger(STOP);
    private Serializer serializer;

    private AbstractContext context;

    private static final int STOP = 0;
    private static final int RUNNING = 1;

    public NettyServer(AbstractContext context){
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

    public void start(){
        if (status.compareAndSet(STOP, RUNNING)) {
            start0();
        }
    }

    public void stop(){
        if (status.compareAndSet(RUNNING, STOP)) {
            log.info("shutting down...");
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    public boolean isRunning() {
        return status.get() == RUNNING;
    }

    public NettyServer setSerializer(Serializer serializer){
        this.serializer = serializer;
        return this;
    }

    private void start0(){

        serializer = Optional.ofNullable(serializer).orElseGet(ProtoStuffSerializer::new);

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

                                    .addLast(new ChannelServerHandler());
                        }
                    });

            LocalAddressHolder.HOST = NetUtil.getHost();
            LocalAddressHolder.PORT = port;

            ChannelFuture future = bootstrap.bind(port).sync();
            if (future.isSuccess()) {
                context.serverStarting();
                log.info("starting netty server in {}ms, port: {}", System.currentTimeMillis() - start, port);
            }
            future.channel().closeFuture().sync();
        } catch (Exception ex){
            log.error(ex.getMessage(), ex);
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}