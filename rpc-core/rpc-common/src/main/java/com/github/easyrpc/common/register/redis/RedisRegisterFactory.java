package com.github.easyrpc.common.register.redis;

import com.github.easyrpc.common.register.ProviderRegister;
import com.github.easyrpc.common.register.RegisterFactory;
import com.github.easyrpc.common.util.Assert;
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

    /**
     * @param address pwd@IP:port
     * @return
     */
    @Override
    public synchronized ProviderRegister getInstance(String address) {

        Assert.notNull(address, "redis链接地址不能为空");
        String pwd = address.split("@")[0];
        String host = address.split("@")[1].split(":")[0];
        String port = address.split("@")[1].split(":")[1];

        String key = host + "==" + port;
        if (!REGISTER_MAP.containsKey(key)){
            REGISTER_MAP.put(key, new RedisRegister(host, Integer.valueOf(port), pwd));
        }
        
        return REGISTER_MAP.get(key);
    }

    @Override
    public String getProtocol() {
        return "redis";
    }

}
