package com.github.easyrpc.common.serialize;

import com.github.easyrpc.common.serialize.hessian.HessianSerializer;
import com.github.easyrpc.common.serialize.kryo.KryoSerializer;
import com.github.easyrpc.common.serialize.protostuff.ProtoStuffSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name SerializeFactory
 * @Date 2020/11/02 10:39
 */
public class SerializeFactory {

    private static final Map<String, Serializer> serializerMap = new HashMap<>(3);

    static {
        serializerMap.put(HessianSerializer.NAME, new HessianSerializer());
        serializerMap.put(KryoSerializer.NAME, new KryoSerializer());
        serializerMap.put(ProtoStuffSerializer.NAME, new ProtoStuffSerializer());
    }

    public static Serializer getSerializer(String serializeName){
        return serializerMap.get(serializeName);
    }

    public static Serializer getDefaultSerializer(){
        return new ProtoStuffSerializer();
    }

}
