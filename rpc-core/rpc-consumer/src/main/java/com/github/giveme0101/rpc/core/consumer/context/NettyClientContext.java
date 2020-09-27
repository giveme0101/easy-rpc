package com.github.giveme0101.rpc.core.consumer.context;

import com.github.giveme0101.rpc.core.common.entity.RpcRequest;
import com.github.giveme0101.rpc.core.common.entity.RpcResponse;
import com.github.giveme0101.rpc.core.common.exception.RpcException;
import com.github.giveme0101.rpc.core.common.register.ProviderDiscovery;
import com.github.giveme0101.rpc.core.consumer.client.ChannelProvider;
import com.github.giveme0101.rpc.core.consumer.util.ProcessingRequests;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name ClientContext
 * @Date 2020/09/14 10:23
 */
@Slf4j
public class NettyClientContext extends AbstractContext {

    private ChannelProvider channelProvider;

    public NettyClientContext(){
        super();
        channelProvider = new ChannelProvider();
    }

    public void setProviderDiscovery(ProviderDiscovery discovery){
        this.providerDiscover = discovery;
    }

    @Override
    public CompletableFuture<RpcResponse> sendRequest(RpcRequest request) {

        CompletableFuture<RpcResponse> resultFuture = new CompletableFuture<>();

        InetSocketAddress provider = providerDiscover.lookupProvider(request.getClassName());
        if (null == provider){
            throw new RpcException("provider not found");
        }

        log.info("discover provider: {}", provider);

        Channel channel = channelProvider.get(provider);
        if (null != channel && channel.isActive()){

            channel.writeAndFlush(request).addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    ProcessingRequests.put(request.getRequestId(), resultFuture);
                    log.info("request {} send success", request.getRequestId());
                } else {
                    future.channel().close();
                    resultFuture.completeExceptionally(future.cause());
                    log.error("request {} send failed:", request.getRequestId(), future.cause());
                }
            });
        } else {
            throw new IllegalStateException();
        }

        return resultFuture;
    }

}