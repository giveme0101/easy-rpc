package com.github.giveme0101.rpc.server.server;

import com.github.giveme0101.rpc.RpcService;
import com.github.giveme0101.rpc.codec.RpcDecoder;
import com.github.giveme0101.rpc.codec.RpcEncoder;
import com.github.giveme0101.rpc.codec.RpcRequest;
import com.github.giveme0101.rpc.codec.RpcResponse;
import com.github.giveme0101.rpc.serialize.Serializer;
import com.github.giveme0101.rpc.serialize.protostuff.ProtoStuffSerializer;
import com.github.giveme0101.rpc.server.handle.ChannelServerHandler;
import com.github.giveme0101.rpc.server.util.Scanner;
import com.github.giveme0101.rpc.server.util.ServiceMapHolder;
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

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name Server
 * @Date 2020/09/14 13:57
 */
@Slf4j
public class RpcServer implements IServer {

    private EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private EventLoopGroup workGroup = new NioEventLoopGroup(4);

    private int port;
    private String implPackage;
    private long start;
    private AtomicInteger status = new AtomicInteger(STOP);
    private Serializer serializer;

    private static final int STOP = 0;
    private static final int RUNNING = 1;

    public RpcServer(int port){
        this.port = port;
        start = System.currentTimeMillis();
    }

    public RpcServer(int port, String implPackage){
        this(port);
        this.implPackage = implPackage;
    }

    public RpcServer(int port, String implPackage, Serializer serializer){
        this(port, implPackage);
        this.serializer = serializer;
    }

    @Override
    public void start(){
        if (status.compareAndSet(STOP, RUNNING)) {
            scan();
            start0();
        }
    }

    @Override
    public void stop(){
        if (status.compareAndSet(RUNNING, STOP)) {
            log.info("shutting down...");
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    @Override
    public boolean isRunning() {
        return status.get() == RUNNING;
    }

    public RpcServer setImplPackage(String implPackage){
        this.implPackage = implPackage;
        return this;
    }

    public RpcServer setSerializer(Serializer serializer){
        this.serializer = serializer;
        return this;
    }

    private void start0(){

        if (null == serializer){
            serializer = new ProtoStuffSerializer();
        }

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

            ChannelFuture future = bootstrap.bind(port).sync();
            if (future.isSuccess()) {
                log.info("starting server in {}ms, port: {}", System.currentTimeMillis() - start, port);
            }
            future.channel().closeFuture().sync();
        } catch (Exception ex){
            log.error(ex.getMessage(), ex);
        }
    }

    private void scan(){
        if (null == implPackage){
            throw new NullPointerException("invalid service.impl.package");
        }

        List<Class> clazzList = Scanner.scan(implPackage);
        clazzList.stream()
                .filter(clz -> clz.isAnnotationPresent(RpcService.class))
                .forEach(impl -> {
                    log.info("scan impl {}", impl);
                    ServiceMapHolder.put(impl);
                });
    }

}