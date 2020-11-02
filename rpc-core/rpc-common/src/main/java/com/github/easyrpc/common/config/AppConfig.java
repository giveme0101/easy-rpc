package com.github.easyrpc.common.config;

import com.github.easyrpc.common.register.RegisterProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name AppConfig
 * @Date 2020/11/02 13:44
 */
@Getter
@Setter
public class AppConfig {

    /**
     * 注册中心配置
     */
    private RegisterProperties registerProperties;

    /**
     * 序列化协议
     */
    private String serializerProtocol;

    /**
     * 注册中心工厂类
     */
    private String providerRegisterClass;

    /**
     * 注册中心筛选
     */
    private String providerRegisterName;

}
