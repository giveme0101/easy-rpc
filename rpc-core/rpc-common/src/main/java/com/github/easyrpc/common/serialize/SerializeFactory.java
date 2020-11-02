package com.github.easyrpc.common.serialize;

import com.github.easyrpc.common.serialize.hessian.HessianSerializer;
import com.github.easyrpc.common.serialize.kryo.KryoSerializer;
import com.github.easyrpc.common.serialize.protostuff.ProtoStuffSerializer;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name SerializeFactory
 * @Date 2020/11/02 10:39
 */
public class SerializeFactory {

    public static Serializer getSerializer(String serializeName){

        Serializer serializer = null;

        switch (serializeName){
            case HessianSerializer.NAME:
                serializer = new HessianSerializer();
                break;
            case KryoSerializer.NAME:
                serializer = new KryoSerializer();
                break;
            case ProtoStuffSerializer.NAME:
                serializer = new ProtoStuffSerializer();
                break;
        }

        return serializer;
    }

    public static Serializer getDefaultSerializer(){
        return new ProtoStuffSerializer();
    }

}
