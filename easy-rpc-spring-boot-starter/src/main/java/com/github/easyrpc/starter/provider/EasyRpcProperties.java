package com.github.easyrpc.starter.provider;

import com.github.easyrpc.common.register.RegisterProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name RegisterProperties
 * @Date 2020/10/29 13:37
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "easy-rpc")
public class EasyRpcProperties {

    /**
     * 注册中心配置
     */
    private RegisterProperties registry;
    /**
     * 序列化方式
     */
    private String serializer;

}
