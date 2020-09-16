package com.github.giveme0101.rpc.server.handle;

import com.github.giveme0101.rpc.server.util.MethodInvoker;
import com.github.giveme0101.rpc.codec.RpcRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name ChannelServerHandler
 * @Date 2020/09/14 14:05
 */
@Slf4j
public class ChannelServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest request) {
        log.debug("channelRead");
        ctx.writeAndFlush(MethodInvoker.invoke(request));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.debug("channelActive");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.debug("channelInactive");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.debug("exceptionCaught: {}", cause.getMessage());
    }

}
