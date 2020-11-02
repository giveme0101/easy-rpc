package com.github.easyrpc.core.provider.context;

import com.github.easyrpc.common.entity.RpcServiceReference;
import com.github.easyrpc.common.exception.RpcException;
import com.github.easyrpc.common.util.RpcErrorMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name ServiceProviderImpl
 * @Date 2020/09/22 10:36
 */
@Slf4j
public class ServiceProviderImpl implements ServiceProvider {

    protected Map<String, Object> serviceMap;

    public ServiceProviderImpl(){
        serviceMap = new HashMap<>();
    }

    @Override
    public void addService(Object service, RpcServiceReference rpcServiceProperties) {

        String serviceName = rpcServiceProperties.getServiceName();

        if (serviceMap.containsKey(serviceName)) {
            return;
        }

        serviceMap.put(serviceName, service);
        log.debug("add service {} -> {}", serviceName, service.getClass().getName());
    }

    @Override
    public Object getService(RpcServiceReference rpcServiceProperties) {

        String serviceName = rpcServiceProperties.getServiceName();

        Object service = serviceMap.get(serviceName);
        if (service != null) {
            return service;
        }

        throw new RpcException(RpcErrorMessage.SERVICE_CAN_NOT_BE_FOUND);
    }

    @Override
    public Iterator<Map.Entry<String, Object>> iterator() {
        return serviceMap.entrySet().iterator();
    }

}
