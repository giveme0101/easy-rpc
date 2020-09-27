package com.github.giveme0101.rpc.core.provider.context;

import com.github.giveme0101.rpc.core.common.IContext;
import com.github.giveme0101.rpc.core.common.entity.RpcServiceProperties;
import com.github.giveme0101.rpc.core.common.register.ProviderRegister;
import com.github.giveme0101.rpc.core.common.util.PropertiesReadable;
import com.github.giveme0101.rpc.core.common.util.PropertiesReader;
import com.github.giveme0101.rpc.core.common.util.SingletonFactory;
import com.github.giveme0101.rpc.core.provider.util.RpcScanner;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name AbstractContext
 * @Date 2020/09/21 15:11
 */
@Slf4j
public abstract class AbstractContext extends Observable implements IContext, PropertiesReadable, ServiceProvider {

    protected ServiceProvider serviceProvider;
    protected PropertiesReader propertiesReader;

    public AbstractContext(){

        propertiesReader = new PropertiesReader("application.properties");
        serviceProvider = SingletonFactory.getInstance(ServiceProviderImpl.class);

        String rpcPackage = propertiesReader.getProperty("service.impl.package");
        RpcScanner.scanRpcService(rpcPackage, serviceProvider);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            stop();
        }));

    }

    @Override
    public void start() {
        startServer();
        contextStarting();
    }

    @Override
    public void stop() {
        contextStop();
        stopServer();
    }

    public void setProviderRegister(ProviderRegister register) {
        this.addObserver(new RegistrySupport(register));
    }

    public void addEventListener(Observer observer){
        this.addObserver(observer);
    }

    public void contextStarting(){
        this.setChanged();
        this.notifyObservers(EventEnum.STARTING);
    }

    public void serverStarting(){
        this.setChanged();
        this.notifyObservers(EventEnum.SERVER_STARTING);
    }

    public void contextStop(){
        this.setChanged();
        this.notifyObservers(EventEnum.STOP);
    }

    @Override
    public void loadProperties(String... configPath) {
        propertiesReader.loadProperties(configPath);
    }

    @Override
    public String getProperty(String key) {
        return propertiesReader.getProperty(key);
    }

    @Override
    public void addService(Object service, RpcServiceProperties rpcServiceProperties) {
        serviceProvider.addService(service, rpcServiceProperties);
    }

    @Override
    public Object getService(RpcServiceProperties rpcServiceProperties) {
        return serviceProvider.getService(rpcServiceProperties);
    }

    @Override
    public Map<String, Object> getServiceMap() {
        return serviceProvider.getServiceMap();
    }

    protected abstract void startServer();

    protected abstract void stopServer();

}
