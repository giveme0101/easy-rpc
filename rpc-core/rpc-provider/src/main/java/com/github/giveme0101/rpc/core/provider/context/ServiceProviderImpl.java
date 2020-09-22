package com.github.giveme0101.rpc.core.provider.context;

import com.github.giveme0101.rpc.core.common.entity.RpcServiceProperties;
import com.github.giveme0101.rpc.core.common.exception.RpcException;
import com.github.giveme0101.rpc.core.common.util.RpcErrorMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
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
    public void addService(Object service, RpcServiceProperties rpcServiceProperties) {

        String serviceName = rpcServiceProperties.getServiceName();

        if (serviceMap.containsKey(serviceName)) {
            return;
        }

        serviceMap.put(serviceName, service);
        log.debug("add service {} -> {}", serviceName, service.getClass().getName());
    }

    @Override
    public Object getService(RpcServiceProperties rpcServiceProperties) {

        String serviceName = rpcServiceProperties.getServiceName();

        Object service = serviceMap.get(serviceName);
        if (service != null) {
            return service;
        }

        throw new RpcException(RpcErrorMessage.SERVICE_CAN_NOT_BE_FOUND);
    }

    @Override
    public Map<String, Object> getServiceMap() {
        return new HashMap<>(serviceMap);
    }

}
