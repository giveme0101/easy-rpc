package com.github.giveme0101.rpc.core.common.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name PropertiesReader
 * @Date 2020/09/27 15:20
 */
@Slf4j
public class PropertiesReader implements PropertiesReadable {

    private Properties properties;

    public PropertiesReader() {
        this.properties = new Properties();
    }

    public PropertiesReader(String... configPath) {
        this.properties = new Properties();
        this.loadProperties(configPath);
    }

    @Override
    public void loadProperties(String... configPath) {
        for (final String path : configPath) {
            try {
                URL resource = PropertiesReader.class.getClassLoader().getResource(path);
                if (null == resource){
                    throw new RuntimeException("读取配置文件[" + path + "]失败：");
                }

                InputStream inputStream = resource.openStream();
                properties.load(inputStream);
            } catch (IOException ex){
                log.error("读取配置文件[{}]失败：", path, ex.getMessage(), ex);
                throw new RuntimeException("读取配置文件[" + path + "]失败", ex);
            }
        }
    }

    @Override
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
