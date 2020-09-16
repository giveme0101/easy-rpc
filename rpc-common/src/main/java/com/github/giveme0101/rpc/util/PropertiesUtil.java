package com.github.giveme0101.rpc.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name PropertiesUtil
 * @Date 2020/09/16 13:56
 */
@Slf4j
public class PropertiesUtil {

    public static Properties load(String path){

        try {

            URL resource = PropertiesUtil.class.getClassLoader().getResource(path);
            if (null == resource){
                throw new RuntimeException("读取配置文件[" + path + "]失败：");
            }

            InputStream inputStream = resource.openStream();

            Properties properties = new Properties();
            properties.load(inputStream);

            return properties;
        } catch (IOException ex){
            log.error("读取配置文件[{}]失败：", path, ex.getMessage(), ex);
            throw new RuntimeException("读取配置文件[" + path + "]失败", ex);
        }
    }

}
