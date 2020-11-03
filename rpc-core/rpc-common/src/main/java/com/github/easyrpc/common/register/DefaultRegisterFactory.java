package com.github.easyrpc.common.register;

import com.github.easyrpc.common.register.redis.RedisRegisterFactory;
import com.github.easyrpc.common.spi.SPILoader;
import com.github.easyrpc.common.util.Assert;
import com.github.easyrpc.common.util.ReflectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name DefaultRegisterFactory
 * @Date 2020/11/02 15:58
 */
public class DefaultRegisterFactory {

    private static final Map<String, Class<? extends RegisterFactory>> declaredRegistryMap = new HashMap<>(2);
    static {
        declaredRegistryMap.put("redis", RedisRegisterFactory.class);
    }

    public static ProviderRegister getProviderRegister(String registryAddress, ClassLoader classLoader) {

        Assert.notNull(registryAddress, "registry address is null");
        String[] address = registryAddress.split("://");

        String protocol = address[0];
        Assert.notNull(protocol, "protocol is null");

        Class<? extends RegisterFactory> aClass = declaredRegistryMap.get(protocol);
        if (null != aClass) {
            try {
                RegisterFactory registerFactory = ReflectionUtils.accessibleConstructor(aClass).newInstance();
                return registerFactory.getInstance(address[1]);
            } catch (Exception ex) {
                throw new RuntimeException("实例化失败！");
            }
        }

        List<RegisterFactory> providerRegisterList = SPILoader.loadFactories(RegisterFactory.class, classLoader);
        Assert.notEmpty(providerRegisterList, "load provider register error: not found");

        for (final RegisterFactory factory : providerRegisterList) {
            if (StringUtils.equals(protocol, factory.getProtocol())) {
                return factory.getInstance(address[1]);
            }
        }

        throw new RuntimeException("获取失败！");
    }

}
