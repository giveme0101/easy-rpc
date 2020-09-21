package com.github.giveme0101.rpc.core.client.util;

import com.github.giveme0101.rpc.core.common.codec.RpcRequest;
import com.github.giveme0101.rpc.core.common.codec.RpcResponse;
import com.github.giveme0101.rpc.core.client.handle.ResponseFuture;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name ResponseFutureHolder
 * @Date 2020/09/16 9:44
 */
public class ResponseFutureHolder {

    private static Map<String, ResponseFuture> HOLDING_FUTURE_MAP = new ConcurrentHashMap();

    public static ResponseFuture get(RpcRequest request){
        ResponseFuture future = new ResponseFuture(request);
        HOLDING_FUTURE_MAP.put(request.getRequestId(), future);
        return future;
    }

    public static void put(RpcResponse response){
        ResponseFuture future = HOLDING_FUTURE_MAP.get(response.getRequestId());
        future.done(response);
        HOLDING_FUTURE_MAP.remove(response.getRequestId());
    }

}
