package com.github.giveme0101.rpc.codec;

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

    private String className;

    private String methodName;

    private Class<?>[] parameterTypes;

    private Object[] parameters;

    private String version;

}
