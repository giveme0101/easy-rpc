package com.github.giveme0101.rpc.core.common.exception;

import com.github.giveme0101.rpc.core.common.entity.ResponseCodeEnum;
import com.github.giveme0101.rpc.core.common.entity.RpcRequest;
import com.github.giveme0101.rpc.core.common.entity.RpcResponse;
import com.github.giveme0101.rpc.core.common.util.RpcErrorMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * Verify RpcRequest and RpcRequest
 *
 * @author shuang.kou
 * @createTime 2020年05月26日 18:03:00
 */
@Slf4j
public final class RpcMessageChecker {

    private RpcMessageChecker() {
    }

    public static void check(RpcResponse<Object> rpcResponse, RpcRequest rpcRequest) {

        if (rpcResponse == null) {
            throw new RpcException(RpcErrorMessage.SERVICE_INVOCATION_FAILURE);
        }

        if (!rpcRequest.getRequestId().equals(rpcResponse.getRequestId())) {
            throw new RpcException(RpcErrorMessage.REQUEST_NOT_MATCH_RESPONSE);
        }

        if (!rpcResponse.getCode().equals(ResponseCodeEnum.SUCCESS.getCode())){
            throw new RpcException(rpcResponse.getCode(), rpcResponse.getMessage());
        }
    }
}
