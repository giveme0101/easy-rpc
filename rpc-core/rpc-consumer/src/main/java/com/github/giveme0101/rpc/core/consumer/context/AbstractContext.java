package com.github.giveme0101.rpc.core.consumer.context;

import com.github.giveme0101.RedisRegister;
import com.github.giveme0101.rpc.core.common.config.RegistryConfig;
import com.github.giveme0101.rpc.core.common.register.ProviderDiscovery;
import com.github.giveme0101.rpc.core.common.util.PropertiesUtil;

import java.util.Properties;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name AbstractContext
 * @Date 2020/09/22 9:21
 */
public abstract class AbstractContext implements AbstractProxyFactory{

    protected ProviderDiscovery providerDiscover;

    public AbstractContext(){
        loadConfigProperties();
    }

    private void loadConfigProperties() {
        Properties properties = PropertiesUtil.load("application.properties");
        RegistryConfig registerConfig = PropertiesUtil.parseRegistryConfig(properties);
        providerDiscover = RedisRegister.getInstance(registerConfig.getServerHost(), registerConfig.getPort(), registerConfig.getPassword());
        afterPropertiesRead(properties);
    }

    protected abstract void afterPropertiesRead(Properties properties);

}
