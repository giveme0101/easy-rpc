package com.github.giveme0101.rpc.codec;

import com.github.giveme0101.rpc.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name RpcDecoder
 * @Date 2020/09/15 9:27
 */
@Slf4j
public class RpcDecoder extends ByteToMessageDecoder {

    private Class<?> genericClass;
    private Serializer serializer;

    public RpcDecoder(Class clazz, Serializer serializer){
        this.genericClass = clazz;
        this.serializer = serializer;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        try {
            if (in.readableBytes() < 4) {
                return;
            }

            in.markReaderIndex();

            int length = in.readInt();
            if (in.readableBytes() < length) {
                in.resetReaderIndex();
                return;
            }

            byte[] data = new byte[length];
            in.readBytes(data);
            out.add(serializer.deserialize(data, genericClass));
        } catch (Exception ex){
            log.error(ex.getMessage(), ex);
        }
    }

}
