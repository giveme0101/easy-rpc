package com.github.giveme0101.rpc.client.client;

import com.github.giveme0101.rpc.client.handle.ChannelClientHandler;
import com.github.giveme0101.rpc.codec.RpcDecoder;
import com.github.giveme0101.rpc.codec.RpcEncoder;
import com.github.giveme0101.rpc.codec.RpcRequest;
import com.github.giveme0101.rpc.codec.RpcResponse;
import com.github.giveme0101.rpc.constant.AppConstants;
import com.github.giveme0101.rpc.serialize.Serializer;
import com.github.giveme0101.rpc.serialize.protostuff.ProtoStuffSerializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name Client
 * @Date 2020/09/14 10:35
 */
@Slf4j
public class RpcClient implements IClient {

    private EventLoopGroup bossGroup = new NioEventLoopGroup(1);

    private String host;
    private int port;
    private AtomicInteger status = new AtomicInteger(AppConstants.STOP);

    public RpcClient(String ip, int port){
        this.host = ip;
        this.port = port;
    }

    @Override
    public void start(){
        if (status.compareAndSet(AppConstants.STOP, AppConstants.RUNNING)) {
            doStart();
        }
    }

    @Override
    public void stop(){
        if (status.compareAndSet(AppConstants.RUNNING, AppConstants.STOP)) {
            bossGroup.shutdownGracefully();
        }
    }

    @Override
    public boolean isRunning() {
        return status.get() == AppConstants.RUNNING;
    }

    private void doStart(){
        try {
            Bootstrap client = new Bootstrap()
                    .group(bossGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {

                            Serializer serializer = new ProtoStuffSerializer();

                            nioSocketChannel.pipeline()

                                    .addLast(new RpcEncoder(RpcRequest.class, serializer))
                                    .addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 0))
                                    .addLast(new RpcDecoder(RpcResponse.class, serializer))

                                    .addLast(new ChannelClientHandler());
                        }
                    });

            ChannelFuture channelFuture = client.connect(host, port).sync();
            if (channelFuture.isSuccess()) {
                log.info("连接服务器成功: {}:{}", host, port);
            }

            channelFuture.channel().closeFuture();
        } catch (Exception ex){
            log.error("连接服务器失败: {}", ex.getMessage(), ex);
            status.set(AppConstants.STOP);
            Runtime.getRuntime().exit(0);
        }
    }

}