package com.github.giveme0101.rpc.core.server.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name ServiceMapHolder
 * @Date 2020/09/14 16:34
 */
public class ServiceMapHolder {

    private static final Map<Class, Class> interfaceMap = new HashMap<>();

    public static void put(Class clz){
        Class[] interfaces = clz.getInterfaces();
        for (final Class anInterface : interfaces) {
            interfaceMap.put(anInterface, clz);
        }
    }

    public static Class get(Class interfaceName){
        return interfaceMap.get(interfaceName);
    }

}
