package com.github.giveme0101.rpc.core.provider.util;

import com.github.giveme0101.rpc.core.common.entity.RpcRequest;
import com.github.giveme0101.rpc.core.common.exception.RpcException;
import com.github.giveme0101.rpc.core.common.util.SingletonFactory;
import com.github.giveme0101.rpc.core.provider.context.ServiceProvider;
import com.github.giveme0101.rpc.core.provider.context.ServiceProviderImpl;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name MethodInvoker
 * @Date 2020/09/14 15:55
 */
@Slf4j
public class RpcRequestHandler {

    private final ServiceProvider serviceProvider;

    public RpcRequestHandler(){
        serviceProvider = SingletonFactory.getInstance(ServiceProviderImpl.class);
    }

    public Object handle(RpcRequest request){
        Object service = serviceProvider.getService(request.toRpcProperties());
        return invokeMethod(service, request);
    }

    public Object invokeMethod(Object service, RpcRequest request){

        try {
            String methodName = request.getMethodName();
            Object[] parameters = request.getParameters();
            Method declaredMethod = service.getClass().getDeclaredMethod(methodName, request.getParameterTypes());

            log.debug("invoke method: {}.{}({})", service.getClass().getName(), methodName, parameters);
            Object result = declaredMethod.invoke(service, parameters);
            log.debug("result: {}", result);

            return result;
        } catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException ex) {
            log.error(ex.getMessage(), ex);
            throw new RpcException(ex);
        } catch (InvocationTargetException ex){
            log.error(ex.getMessage(), ex.getCause());
            throw new RpcException(ex.getCause());
        }
    }

}
