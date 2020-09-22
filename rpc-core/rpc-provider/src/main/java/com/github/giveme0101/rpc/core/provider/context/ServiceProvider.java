package com.github.giveme0101.rpc.core.provider.context;

import com.github.giveme0101.rpc.core.common.entity.RpcServiceProperties;

import java.util.Map;

/**
 * store and provide service object.
 *
 * @author shuang.kou
 * @createTime 2020年05月31日 16:52:00
 */
public interface ServiceProvider {

    void addService(Object service, RpcServiceProperties rpcServiceProperties);

    Object getService(RpcServiceProperties rpcServiceProperties);

    Map<String, Object> getServiceMap();

}
