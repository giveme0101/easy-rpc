package com.github.easyrpc.common.constant;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name AppConstants
 * @Date 2020/10/30 10:25
 */
public class AppConstants {

    /**
     * 注册中心实例名，用于SPI加载了多个时选择
     */
    public static final String PROVIDER_REGISTER_NAME = "registry.provider.name";
    /**
     * 注册中心实现类
     */
    public static final String PROVIDER_REGISTER_TYPE = "registry.provider.class";
    public static final String K_REGISTRY_HOST = "registry.host";
    public static final String K_REGISTRY_PORT = "registry.port";
    public static final String K_REGISTRY_PWD = "registry.password";

    /**
     * 扫描服务实现包
     */
    public static final String K_SERVICE_IMPL_PACKAGE = "service.impl.package";

    /**
     * 配置文件位置
     */
    public static final  String PROPERTY_LOCATION = "easy-rpc.properties";

    /**
     * 序列号协议
     */
    public static final String SERIALIZER_PROTOCOL = "protocol.serialization";


}
