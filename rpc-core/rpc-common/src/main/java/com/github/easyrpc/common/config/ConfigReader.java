package com.github.easyrpc.common.config;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name ConfigReader
 * @Date 2020/11/02 10:08
 */
public interface ConfigReader {

    String getValue(String key);

    String getValue(String key, String ofDefault);

}
