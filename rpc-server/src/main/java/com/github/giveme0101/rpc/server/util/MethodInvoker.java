package com.github.giveme0101.rpc.server.util;

import com.github.giveme0101.rpc.codec.RpcRequest;
import com.github.giveme0101.rpc.codec.RpcResponse;
import com.github.giveme0101.rpc.exception.RpcException;
import com.github.giveme0101.rpc.exception.RpcExecuteException;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name MethodInvoker
 * @Date 2020/09/14 15:55
 */
@Slf4j
public class MethodInvoker {

    public static RpcResponse invoke(RpcRequest request){

        String className = request.getClassName();
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();

        try {
            Class<?> aClass = Class.forName(className);

            Method declaredMethod = aClass.getDeclaredMethod(methodName, parameterTypes);

            Class instanceClass = ServiceMapHolder.get(aClass);
            Object instance = instanceClass.newInstance();

            try {
                log.debug("invoke method: {}.{}({})", instanceClass.getName(), methodName, parameters);
                Object result = declaredMethod.invoke(instance, parameters);
                log.debug("result: {}", result);
                return RpcResponse.builder()
                        .requestId(request.getRequestId())
                        .result(result)
                        .build();
            } catch (Exception ex) {
                log.warn("invoke error: ", ex.getCause() == null ? null : ex.getCause().getMessage(), ex.getCause());
                return RpcResponse.builder()
                        .requestId(request.getRequestId())
                        .error(new RpcExecuteException(ex.getCause()))
                        .build();
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return RpcResponse.builder()
                    .requestId(request.getRequestId())
                    .error(new RpcException(ex))
                    .build();
        }
    }

}
