package com.github.easyrpc.core.consumer.factory;

import com.github.easyrpc.common.entity.RpcRequest;
import com.github.easyrpc.common.entity.RpcResponse;
import com.github.easyrpc.common.exception.RpcException;
import com.github.easyrpc.common.register.ProviderRegister;
import com.github.easyrpc.core.consumer.client.ChannelProvider;
import com.github.easyrpc.core.consumer.util.ProcessingRequests;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name DefaultProxyFactory
 * @Date 2020/10/29 14:49
 */
@Slf4j
public class DefaultProxyFactory extends AbstractProxyFactory {

    private ProviderRegister providerDiscover;
    private ChannelProvider channelProvider;

    public DefaultProxyFactory(ProviderRegister providerDiscover, ChannelProvider channelProvider) {
        this.providerDiscover = providerDiscover;
        this.channelProvider = channelProvider;
    }

    @Override
    public CompletableFuture<RpcResponse> sendRequest(RpcRequest request) {

        CompletableFuture<RpcResponse> resultFuture = new CompletableFuture<>();

        String serviceKey = request.getClassName() + "::" + request.getVersion();

        InetSocketAddress provider = providerDiscover.lookupProvider(serviceKey);
        if (null == provider){
            throw new RpcException("provider not found");
        }

        log.info("discover provider: {}", provider);

        Channel channel = channelProvider.getChannel(provider);
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
