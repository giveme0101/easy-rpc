package com.github.easyrpc.common.context;

import com.github.easyrpc.common.config.AppConfig;
import com.github.easyrpc.common.entity.ContextEvent;
import com.github.easyrpc.common.register.DefaultRegisterFactory;
import com.github.easyrpc.common.register.ProviderRegister;
import com.github.easyrpc.common.serialize.SerializeFactory;
import com.github.easyrpc.common.serialize.Serializer;
import com.github.easyrpc.common.spi.SPILoader;
import com.github.easyrpc.common.util.Assert;
import com.github.easyrpc.common.util.BannerPrinter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name AbstractContext
 * @Date 2020/10/29 17:32
 */
@Slf4j
public abstract class AbstractContext extends ObservableContext implements IContext {

    /**
     * 服务注册发现中心
     */
    protected ProviderRegister providerRegister;
    /**
     * 序列化方式
     */
    protected Serializer serializer;
    protected AppConfig appConfig;
    protected ClassLoader classLoader;
    private AtomicBoolean contextRunning;

    public AbstractContext(AppConfig appConfig){
        this.prepareContext();
        this.readConfig(appConfig);
        this.printBanner();
        this.initRegister();
        this.refresh();
    }

    @Override
    public void run() {
        this.publish(ContextEvent.STARTING);
        log.info("context started");
        log.debug("start http server...");
        startServer();
    }

    @Override
    public void close() {
        if (contextRunning.compareAndSet(true, false)) {
            log.info("stopping context...");
            this.publish(ContextEvent.PRE_CLOSE);
            log.debug("closing http server...");
            stopServer();
        }
    }

    private void prepareContext(){
        contextRunning = new AtomicBoolean(true);
        this.classLoader = this.getClass().getClassLoader();
    }

    private void readConfig(AppConfig config){
        Assert.notNull(config, "app config is null");
        appConfig = this.onReadConfig(config);
    }

    private void printBanner(){
        BannerPrinter.print();
    }

    private void preRefresh() {
        log.info("context post refresh");
        loadSerializer();
        onPreRefresh();
    }

    private void refresh(){
        this.preRefresh();
        log.info("context refresh");
        onRefresh();
        this.postRefresh();
    }

    private void postRefresh(){
        log.info("context post refresh");
        onPostRefresh();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            close();
        }));
    }

    private void loadSerializer(){

        String serializerProtocol = appConfig.getSerializer();
        if (StringUtils.isEmpty(serializerProtocol)){
            serializer = SerializeFactory.getDefaultSerializer();
            log.info("use default serializer: {}", serializer.getClass().getName());
            return;
        }

        serializer = SerializeFactory.getSerializer(serializerProtocol);
        if (null != serializer){
            log.info("use serializer: {}", serializer.getClass().getName());
            return;
        }

        List<Serializer> customerSerializerList = SPILoader.loadFactories(Serializer.class, classLoader);
        if (customerSerializerList != null && !customerSerializerList.isEmpty()){
            for (final Serializer customerSerializer : customerSerializerList) {
                if (StringUtils.equals(customerSerializer.getName(), serializerProtocol)){
                    serializer = customerSerializer;
                    log.info("use customer serializer: {}", serializer.getClass().getName());
                    return;
                }
            }
        }
    }

    private void initRegister(){

        String registryAddress = appConfig.getRegistryAddress();
        Assert.notNull(registryAddress, "registry address is null");

        providerRegister = DefaultRegisterFactory.getProviderRegister(registryAddress, classLoader);
        log.info("use register center: {}", this.providerRegister.getClass().getName());
    }

    protected AppConfig onReadConfig(AppConfig config){
        return config;
    }

    protected void onPreRefresh() {
        // NOOP
    }

    protected abstract void onRefresh();

    protected void onPostRefresh(){
        // NOOP
    }

    protected abstract void startServer();

    protected abstract void stopServer();

}
