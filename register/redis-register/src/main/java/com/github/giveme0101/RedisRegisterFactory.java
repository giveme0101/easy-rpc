package com.github.giveme0101;

import com.github.easyrpc.common.register.ProviderRegister;
import com.github.easyrpc.common.register.RegisterFactory;
import com.github.easyrpc.common.register.RegisterProperties;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name RedisRegisterFactory
 * @Date 2020/09/21 10:49
 */
@Slf4j
public class RedisRegisterFactory implements RegisterFactory {

    private static Map<String, RedisRegister> REGISTER_MAP = new HashMap<>();

    @Override
    public synchronized ProviderRegister getInstance(RegisterProperties properties) {

        String key = properties.getHost() + "==" + properties.getPort();
        if (!REGISTER_MAP.containsKey(key)){
            REGISTER_MAP.put(key, new RedisRegister(properties.getHost(), properties.getPort(), properties.getPassword()));
        }
        
        return REGISTER_MAP.get(key);
    }

    @Override
    public String getName() {
        return "redis-register";
    }
}
