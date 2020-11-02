package com.github.easyrpc.common.spi;

import com.github.easyrpc.common.util.LinkedMultiValueMap;
import com.github.easyrpc.common.util.MultiValueMap;
import com.github.easyrpc.common.config.PropertiesLoader;
import com.github.easyrpc.common.util.ReflectionUtils;
import com.sun.istack.internal.Nullable;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 *
 * org.springframework.core.io.support.SpringFactoriesLoader
 *
 * @name SPILoader
 * @Date 2020/10/30 9:04
 */
@Slf4j
public class SPILoader {

    public static final String FACTORIES_RESOURCE_LOCATION = "META-INF/easy-rpc.factories";

    private static final Map<ClassLoader, MultiValueMap<String, String>> cache = new ConcurrentHashMap<>();

    public static <T> List<T> loadFactories(Class<T> factoryClass, ClassLoader classLoader){

        if (null == classLoader){
            classLoader = SPILoader.class.getClassLoader();
        }

        List<String> factoryNames = loadFactoryNames(factoryClass, classLoader);
        if (log.isTraceEnabled()) {
            log.trace("Loaded [" + factoryClass.getName() + "] names: " + factoryNames);
        }

        List<T> result = new ArrayList<>(factoryNames.size());
        for (String factoryName : factoryNames) {
            result.add(ReflectionUtils.instantiate(factoryName, factoryClass, classLoader));
        }

        return result;
    }

    public static List<String> loadFactoryNames(Class factoryClass, ClassLoader classLoader){
        String className = factoryClass.getName();
        return loadFactories(classLoader).getOrDefault(className, new ArrayList<>(0));
    }

    private static Map<String, List<String>> loadFactories(@Nullable ClassLoader classLoader) {

        MultiValueMap<String, String> result = cache.get(classLoader);
        if (result != null) {
            return result;
        }

        try {
            Enumeration<URL> urls = (classLoader != null ?
                    classLoader.getResources(FACTORIES_RESOURCE_LOCATION) :
                    ClassLoader.getSystemResources(FACTORIES_RESOURCE_LOCATION));
            result = new LinkedMultiValueMap<>();
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                Properties properties = PropertiesLoader.loadProperties(url);
                for (Map.Entry<?, ?> entry : properties.entrySet()) {
                    String factoryClassName = ((String) entry.getKey()).trim();
                    String values = (String) entry.getValue();
                    for (String factoryName : values.split(",")) {
                        result.add(factoryClassName, factoryName.trim());
                    }
                }
            }
            cache.put(classLoader, result);
            return result;
        }
        catch (IOException ex) {
            throw new IllegalArgumentException("Unable to load factories from location [" +
                    FACTORIES_RESOURCE_LOCATION + "]", ex);
        }
    }

}
