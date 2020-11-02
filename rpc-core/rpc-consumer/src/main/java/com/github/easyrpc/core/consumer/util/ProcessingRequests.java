package com.github.easyrpc.core.consumer.util;

import com.github.easyrpc.common.entity.RpcResponse;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name ProcessingRequest
 * @Date 2020/09/21 17:43
 */
public class ProcessingRequests {

    private static Map<String, CompletableFuture<RpcResponse>> PROCESSING_MAP = new ConcurrentHashMap<>();

    public static void put(String requestId, CompletableFuture<RpcResponse> rpcResponse){
        PROCESSING_MAP.put(requestId, rpcResponse);
    }

    public static void complete(RpcResponse rpcResponse) {
        CompletableFuture<RpcResponse> future = PROCESSING_MAP.remove(rpcResponse.getRequestId());
        if (null != future) {
            future.complete(rpcResponse);
        } else {
            throw new IllegalStateException();
        }
    }

}
