package com.github.easyrpc.common.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name NettyClientFactory
 * @Date 2020/09/21 17:26
 */
public class SingletonFactory {

    private static Map<String, Object> SINGLETON_MAP = new ConcurrentHashMap<>();

    public static <T> T getInstance(Class<T> clazz){

        String key = clazz.getName();
        Object instance = SINGLETON_MAP.get(key);

        if (null == instance) {
            synchronized (clazz) {
                if (null == instance) {
                    try {
                        instance = clazz.getDeclaredConstructor().newInstance();
                        SINGLETON_MAP.put(key, instance);
                    } catch (IllegalAccessException | InstantiationException e) {
                        throw new RuntimeException(e.getMessage(), e);
                    } catch (NoSuchMethodException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return clazz.cast(instance);
    }

    public static void clean(){
        SINGLETON_MAP.clear();
    }

}
