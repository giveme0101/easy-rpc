package com.github.easyrpc.common.entity;

import lombok.*;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name RpcServiceReference
 * @Date 2020/09/22 10:32
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RpcServiceReference {
    /**
     * service version
     */
    private String version;
    /**
     * when the interface has multiple implementation classes, distinguish by group
     */
    private String group;

    private String serviceName;

    private Class interfaceClass;

    private Class implClass;

    public String toRpcServiceName() {
        return this.getServiceName() + this.getGroup() + this.getVersion();
    }

}
