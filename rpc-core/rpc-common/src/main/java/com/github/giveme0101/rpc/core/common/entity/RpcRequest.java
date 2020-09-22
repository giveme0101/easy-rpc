package com.github.giveme0101.rpc.core.common.entity;

import com.github.giveme0101.rpc.core.common.constant.RequestTypeEnum;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name RpcRequest
 * @Date 2020/09/15 9:28
 */
@Data
@Builder
public class RpcRequest implements Serializable {

    private static final long serialVersionUID = -8648794535269244967L;

    private String requestId;

    /**
     * 消息类型：0-心跳 1-普通消息
     */
    @Builder.Default
    private byte type = RequestTypeEnum.MESSAGE.getType();

    private String className;

    private String methodName;

    private Class<?>[] parameterTypes;

    private Object[] parameters;

    private String version;

    public void setType(RequestTypeEnum type) {
        this.type = type.getType();
    }

    public RpcServiceProperties toRpcProperties() {
        return RpcServiceProperties.builder().serviceName(this.getClassName())
                .version(this.getVersion())
                .build();
    }

}
