package com.github.easyrpc.core.provider.server;

import com.github.easyrpc.common.entity.RpcServiceReference;

import java.util.Map;

/**
 * store and provide service object.
 *
 * @author shuang.kou
 * @createTime 2020年05月31日 16:52:00
 */
public interface ServiceProvider extends Iterable<Map.Entry<String, Object>> {

    void addService(Object service, RpcServiceReference rpcServiceProperties);

    Object getService(RpcServiceReference rpcServiceProperties);

}
