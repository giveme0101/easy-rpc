package com.github.giveme0101.rpc.core.client.handle;

public interface ResponseCallback {

    void success(Object result);

    void fail(Throwable error);

}
