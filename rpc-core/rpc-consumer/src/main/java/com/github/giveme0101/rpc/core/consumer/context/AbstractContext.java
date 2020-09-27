package com.github.giveme0101.rpc.core.consumer.context;

import com.github.giveme0101.rpc.core.common.register.ProviderDiscovery;
import com.github.giveme0101.rpc.core.common.util.PropertiesReadable;
import com.github.giveme0101.rpc.core.common.util.PropertiesReader;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name AbstractContext
 * @Date 2020/09/22 9:21
 */
public abstract class AbstractContext implements AbstractProxyFactory, PropertiesReadable {

    protected ProviderDiscovery providerDiscover;
    protected PropertiesReadable propertiesReader;

    public AbstractContext(){
        propertiesReader = new PropertiesReader("application.properties");
    }

    @Override
    public void loadProperties(String... configPath) {
        propertiesReader.loadProperties(configPath);
    }

    @Override
    public String getProperty(String key) {
        return propertiesReader.getProperty(key);
    }

}
