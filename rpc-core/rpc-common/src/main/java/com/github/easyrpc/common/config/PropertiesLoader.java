package com.github.easyrpc.common.config;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name PropertiesReader
 * @Date 2020/09/27 15:20
 */
@Slf4j
public class PropertiesLoader {

    public static Properties loadProperties(String propertyLocation) {
        try {
            URL resource = PropertiesLoader.class.getClassLoader().getResource(propertyLocation);
            if (null == resource){
                throw new RuntimeException("读取配置文件[" + propertyLocation + "]失败：");
            }
            return loadProperties(resource);
        } catch (IOException ex){
            log.error("读取配置文件[{}]失败：", propertyLocation, ex.getMessage(), ex);
            throw new RuntimeException("读取配置文件[" + propertyLocation + "]失败", ex);
        }
    }

    public static Properties loadProperties(URL url) throws IOException {
        Properties properties = new Properties();
        properties.load(url.openStream());
        return properties;
    }
}
