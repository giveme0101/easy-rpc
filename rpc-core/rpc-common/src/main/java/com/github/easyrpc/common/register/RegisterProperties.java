package com.github.easyrpc.common.register;

import lombok.*;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name RegisterProperties
 * @Date 2020/10/29 16:33
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterProperties {

    /**
     * 注册中心地址
     */
    private String host;

    /**
     * 注册中心端口号
     */
    private Integer port;

    /**
     * 注册中心密码
     */
    private String password;

}
