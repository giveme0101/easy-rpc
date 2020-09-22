package com.github.giveme0101.rpc.core.provider.context;

import com.github.giveme0101.rpc.core.common.IContext;
import com.github.giveme0101.rpc.core.common.RpcService;
import com.github.giveme0101.rpc.core.common.entity.RpcServiceProperties;
import com.github.giveme0101.rpc.core.common.util.PropertiesUtil;
import com.github.giveme0101.rpc.core.common.util.SingletonFactory;
import com.github.giveme0101.rpc.core.provider.util.Scanner;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Observable;
import java.util.Properties;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name AbstractContext
 * @Date 2020/09/21 15:11
 */
@Slf4j
public abstract class AbstractContext extends Observable implements IContext {

    protected ServiceProvider serviceProvider;

    public AbstractContext(){
        serviceProvider = SingletonFactory.getInstance(ServiceProviderImpl.class);
        loadConfigProperties();
        start();
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

    protected void loadConfigProperties() {

        Properties properties = PropertiesUtil.load("application.properties");

        String implPackage = properties.getProperty("service.impl.package");
        if (null == implPackage){
            throw new NullPointerException("missing or invalid config properties: service.impl.package");
        }

        List<Class> clazzList = Scanner.scan(implPackage);
        clazzList.stream()
                .filter(clz -> clz.isAnnotationPresent(RpcService.class))
                .forEach(impl -> {
                    RpcService rpcService = (RpcService) impl.getAnnotation(RpcService.class);
                    Class[] interfaces = impl.getInterfaces();
                    for (final Class anInterface : interfaces) {

                        RpcServiceProperties rpcServiceProperties = RpcServiceProperties.builder()
                                .serviceName(anInterface.getName())
                                .version(rpcService.version())
                                .build();
                        Object instance = SingletonFactory.getInstance(impl);
                        serviceProvider.addService(instance, rpcServiceProperties);
                        log.info("scan impl {} -> {}", anInterface.getName(), impl);

                    }
                });

        afterPropertiesRead(properties);
    }

    protected abstract void startServer();

    protected abstract void stopServer();

    protected abstract void afterPropertiesRead(Properties properties);
}
