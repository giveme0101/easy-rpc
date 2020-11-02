package com.github.easyrpc.common.context;

import com.github.easyrpc.common.config.AppConfig;
import com.github.easyrpc.common.config.ConfigReader;
import com.github.easyrpc.common.config.DefaultConfigReader;
import com.github.easyrpc.common.constant.AppConstants;
import com.github.easyrpc.common.entity.ContextEvent;
import com.github.easyrpc.common.register.ProviderRegister;
import com.github.easyrpc.common.register.RegisterFactory;
import com.github.easyrpc.common.register.RegisterProperties;
import com.github.easyrpc.common.register.RegisterPropertiesReader;
import com.github.easyrpc.common.serialize.SerializeFactory;
import com.github.easyrpc.common.serialize.Serializer;
import com.github.easyrpc.common.spi.SPILoader;
import com.github.easyrpc.common.util.Assert;
import com.github.easyrpc.common.util.BannerPrinter;
import com.github.easyrpc.common.util.ReflectionUtils;
import com.github.easyrpc.common.util.SingletonFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Optional;
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
    }

    private void readConfig(AppConfig config){

        ConfigReader configReader = getConfigReader();
        if (null != config){
            appConfig = config;
        } else {
            appConfig = new AppConfig();
        }

        appConfig = this.onPreReadConfig(appConfig);

        if (null == appConfig.getRegisterProperties()){
            RegisterProperties registerProperties = RegisterPropertiesReader.read(configReader);
            appConfig.setRegisterProperties(registerProperties);
        }

        if (StringUtils.isEmpty(appConfig.getSerializerProtocol())){
            String protocolName = configReader.getValue(AppConstants.SERIALIZER_PROTOCOL);
            appConfig.setSerializerProtocol(protocolName);
        }

        if (StringUtils.isEmpty(appConfig.getProviderRegisterClass())){
            String providerClass = configReader.getValue(AppConstants.PROVIDER_REGISTER_TYPE);
            appConfig.setProviderRegisterClass(providerClass);
        }

        if (StringUtils.isEmpty(appConfig.getProviderRegisterName())){
            String providerName = configReader.getValue(AppConstants.PROVIDER_REGISTER_NAME);
            appConfig.setProviderRegisterName(providerName);
        }

        appConfig = this.onPostReadConfig(appConfig, configReader);
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
        SingletonFactory.clean();
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

        String serializerProtocol = appConfig.getSerializerProtocol();
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

        RegisterFactory registerFactory = null;

        String providerClass = appConfig.getProviderRegisterClass();
        if (StringUtils.isNotEmpty(providerClass)){
            // FIXME 类加载器加载不到
            registerFactory = ReflectionUtils.instantiate(providerClass, RegisterFactory.class, null);
        } else {

            List<RegisterFactory> providerRegisterList = SPILoader.loadFactories(RegisterFactory.class, classLoader);
            Assert.notEmpty(providerRegisterList, "load provider register error: not found");
            if (providerRegisterList.size() == 1) {
                registerFactory = providerRegisterList.get(0);
                log.info("load provider register: {}", registerFactory.getClass().getName());
            } else {
                String providerName = appConfig.getProviderRegisterName();
                Optional<RegisterFactory> first = providerRegisterList.stream()
                        .filter(p -> StringUtils.equals(p.getName(), providerName))
                        .findFirst();
                if (first.isPresent()) {
                    registerFactory = first.get();
                } else {
                    throw new RuntimeException("invalid provider register name: " + providerName);
                }
            }
        }

        RegisterProperties registerProperties = appConfig.getRegisterProperties();
        providerRegister = registerFactory.getInstance(registerProperties);
        log.info("use register center: {}", providerRegister.getClass().getName());
    }

    protected ConfigReader getConfigReader(){
        return new DefaultConfigReader();
    }

    protected AppConfig onPreReadConfig(AppConfig config){
        return config;
    }

    protected AppConfig onPostReadConfig(AppConfig config, ConfigReader configReader){
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
