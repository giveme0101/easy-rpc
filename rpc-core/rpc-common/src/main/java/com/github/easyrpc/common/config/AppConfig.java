package com.github.easyrpc.common.config;

import lombok.*;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name AppConfig
 * @Date 2020/11/02 13:44
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppConfig {

    /**
     * 注册中心配置
     */
    private String registryAddress;

    /**
     * 序列化协议
     */
    private String serializer;

}
