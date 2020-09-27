package com.github.giveme0101.rpc.core.common.util;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name PropertiesReadable
 * @Date 2020/09/27 15:16
 */
public interface PropertiesReadable {

    /**
     * 加载配置文件
     *
     * @param configPath
     * @return
     */
    void loadProperties(String... configPath);

    /**
     * 获取配置
     *
     * @param key
     * @return
     */
    String getProperty(String key);

}
