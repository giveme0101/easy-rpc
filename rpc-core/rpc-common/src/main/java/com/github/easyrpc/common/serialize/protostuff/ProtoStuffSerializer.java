package com.github.easyrpc.common.serialize.protostuff;

import com.github.easyrpc.common.serialize.Serializer;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name ProtoStuffSerializer
 * @Date 2020/09/15 9:34
 */
public class ProtoStuffSerializer implements Serializer {

    public static final String NAME = "protostuff";
    private Map<Class<?>, Schema<?>> schemaMap = new ConcurrentHashMap<>();

    private Objenesis objenesis = new ObjenesisStd(true);

    private <T> Schema<T> getSchema(Class<T> clazz){
        return (Schema<T>) schemaMap.computeIfAbsent(clazz, RuntimeSchema::createFrom);
    }

    @Override
    public <T> byte[] serialize(T obj) {

        Class<T> aClass = (Class<T>) obj.getClass();
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);

        try {
            Schema<T> schema = getSchema(aClass);
            return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        } catch (Exception ex) {
            throw new IllegalStateException(ex.getMessage(), ex);
        } finally {
            buffer.clear();
        }
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        try {
            T instance = objenesis.newInstance(clazz);
            Schema<T> schema = getSchema(clazz);
            ProtostuffIOUtil.mergeFrom(data, instance, schema);
            return instance;
        } catch (Exception ex){
            throw new IllegalStateException(ex.getMessage(), ex);
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

}
