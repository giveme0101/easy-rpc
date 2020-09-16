package com.github.giveme0101.rpc.client.handle;

import com.github.giveme0101.rpc.client.util.ResponseFutureHolder;
import com.github.giveme0101.rpc.client.util.SocketChannelHolder;
import com.github.giveme0101.rpc.codec.RpcRequest;
import com.github.giveme0101.rpc.codec.RpcResponse;
import com.github.giveme0101.rpc.constant.RequestTypeEnum;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

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
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.WRITER_IDLE) {
                RpcRequest request = RpcRequest.builder()
                        .requestId(UUID.randomUUID().toString())
                        .type(RequestTypeEnum.HEART_BEAT.getType())
                        .build();
                ctx.channel().writeAndFlush(request);
                log.debug("向服务器发送心跳...");
                return;
            }
        }
        super.userEventTriggered(ctx, evt);
    }

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
        log.error("连接异常，即将与服务器断开连接: {}", cause.getMessage(), cause);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.debug("channelInactive");
        log.info("连接关闭");
        ctx.close();
    }
}