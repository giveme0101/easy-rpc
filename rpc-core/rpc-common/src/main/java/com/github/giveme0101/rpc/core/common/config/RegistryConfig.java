package com.github.giveme0101.rpc.core.common.config;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name ClientConfig
 * @Date 2020/09/21 10:35
 */
@Data
@Accessors(chain = true)
public class RegistryConfig {

    private String serverHost = "127.0.0.1";

    private Integer port = 6379;

    private String password;

}
