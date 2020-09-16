package com.github.giveme0101.rpc.client.handle;

import com.github.giveme0101.rpc.client.util.ResponseFutureHolder;
import com.github.giveme0101.rpc.client.util.SocketChannelHolder;
import com.github.giveme0101.rpc.codec.RpcResponse;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name ChannelHandler
 * @Date 2020/09/14 10:59
 */
@Slf4j
@ChannelHandler.Sharable
public class ChannelClientHandler extends SimpleChannelInboundHandler<RpcResponse> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.debug("channelActive");
        SocketChannelHolder.set(ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse response) {
        log.debug("channelRead");
        ResponseFutureHolder.put(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.debug("exceptionCaught: {}", cause.getMessage());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.debug("channelInactive");
    }
}