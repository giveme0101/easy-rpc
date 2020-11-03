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
    public void addService(Object service, RpcServiceReference rpcServiceReference) {

        String serviceKey = getKey(rpcServiceReference);
        if (serviceMap.containsKey(serviceKey)) {
            return;
        }

        serviceMap.put(serviceKey, service);
        log.debug("add service {} -> {}", serviceKey, service.getClass().getName());
    }

    @Override
    public Object getService(RpcServiceReference rpcServiceReference) {

        String serviceKey = getKey(rpcServiceReference);

        Object service = serviceMap.get(serviceKey);
        if (service != null) {
            return service;
        }

        throw new RpcException(RpcErrorMessage.SERVICE_CAN_NOT_BE_FOUND);
    }

    @Override
    public Iterator<Map.Entry<String, Object>> iterator() {
        return serviceMap.entrySet().iterator();
    }

    private String getKey(RpcServiceReference rpcServiceReference){

        String serviceName = rpcServiceReference.getServiceName();
        String version = rpcServiceReference.getVersion();

        return serviceName + "::" + version;
    }

}
