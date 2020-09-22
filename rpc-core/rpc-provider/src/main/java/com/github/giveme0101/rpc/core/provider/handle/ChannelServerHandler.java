package com.github.giveme0101.rpc.core.provider.handle;

import com.github.giveme0101.rpc.core.common.constant.RequestTypeEnum;
import com.github.giveme0101.rpc.core.common.entity.ResponseCodeEnum;
import com.github.giveme0101.rpc.core.common.entity.RpcRequest;
import com.github.giveme0101.rpc.core.common.entity.RpcResponse;
import com.github.giveme0101.rpc.core.common.exception.RpcException;
import com.github.giveme0101.rpc.core.common.util.SingletonFactory;
import com.github.giveme0101.rpc.core.provider.util.RpcRequestHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.net.SocketAddress;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name ChannelServerHandler
 * @Date 2020/09/14 14:05
 */
@Slf4j
public class ChannelServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private int idleCount = 0;
    private int maxIdle = 2;

    private RpcRequestHandler requestHandler;

    public ChannelServerHandler(){
        requestHandler = SingletonFactory.getInstance(RpcRequestHandler.class);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest request) {
        SocketAddress socketAddress = ctx.channel().remoteAddress();
        log.debug("channelRead - {}: {}", socketAddress.toString(), request);
        if(request.getType() == RequestTypeEnum.HEART_BEAT.getType()){
            log.debug("接收到客户端【{}】的心跳", socketAddress.toString());
            idleCount = 1;
            return;
        }

        try {
            Object result = requestHandler.handle(request);
            ctx.writeAndFlush(RpcResponse.builderSuccess(request.getRequestId(), result));
        } catch (RpcException ex){
            log.debug(ex.getMessage(), ex);
            ctx.writeAndFlush(RpcResponse.builderFail(request.getRequestId(), ResponseCodeEnum.FAIL.getCode(), ex.getMessage()));
        } catch (Exception ex){
            log.error(ex.getMessage(), ex);
            ctx.writeAndFlush(RpcResponse.builderFail(request.getRequestId(), ResponseCodeEnum.INTERNAL_ERROR));
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.debug("channelActive");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.debug("channelInactive");
        log.info("连接关闭: {}", ctx.channel().remoteAddress());
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE && idleCount > maxIdle){
                log.info("连续两次没有收到客户端心跳，断开链接：{}", ctx.channel().remoteAddress());
                ctx.channel().close();
                return;
            }

            idleCount++;
            return;
        }

        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.debug("exceptionCaught");
        log.error("与客户端的连接发生异常，关闭客户端连接: {} - {}", ctx.channel().remoteAddress(), cause.getMessage());
        ctx.close();
    }

}
