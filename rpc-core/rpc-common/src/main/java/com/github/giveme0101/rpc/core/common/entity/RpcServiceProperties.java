package com.github.giveme0101.rpc.core.common.entity;

import lombok.*;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name RpcServiceProperties
 * @Date 2020/09/22 10:32
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RpcServiceProperties {
    /**
     * service version
     */
    private String version;
    /**
     * when the interface has multiple implementation classes, distinguish by group
     */
    private String group;

    private String serviceName;

    public String toRpcServiceName() {
        return this.getServiceName() + this.getGroup() + this.getVersion();
    }

}
