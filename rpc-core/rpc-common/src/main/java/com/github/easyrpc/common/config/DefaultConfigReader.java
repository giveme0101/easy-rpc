package com.github.easyrpc.common.config;

import com.github.easyrpc.common.constant.AppConstants;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name DefaultConfigReader
 * @Date 2020/11/02 10:09
 */
public class DefaultConfigReader implements ConfigReader {

    private final Map<String, Object> configMap = new HashMap<>();

    public DefaultConfigReader() {
        Properties properties = PropertiesLoader.loadProperties(AppConstants.PROPERTY_LOCATION);
        Iterator<Map.Entry<Object, Object>> iterator = properties.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<Object, Object> next = iterator.next();
            configMap.put(String.valueOf(next.getKey()), next.getValue());
        }
    }

    @Override
    public String getValue(String key) {
        Object value = configMap.get(key);
        return null == value ? null : String.valueOf(value);
    }

    @Override
    public String getValue(String key, String ofDefault) {
        Object value = configMap.get(key);
        return null == value ? ofDefault : String.valueOf(value);
    }

}
