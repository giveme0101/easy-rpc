package com.github.giveme0101.rpc.client.handle;

import com.github.giveme0101.rpc.codec.RpcRequest;
import com.github.giveme0101.rpc.codec.RpcResponse;
import com.github.giveme0101.rpc.exception.RpcException;
import com.github.giveme0101.rpc.exception.RpcExecuteException;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name ResponseFuture
 * @Date 2020/09/16 9:06
 */
@Slf4j
public class ResponseFuture <R> implements Future<R> {

    private Sync sync;
    private RpcRequest request;
    private RpcResponse<R> response;
    private long startTime;
    private long threshold = 1000L;
    private Lock lock = new ReentrantLock();
    private List<ResponseCallback> pendingCallbackList = new ArrayList<>();

    public ResponseFuture(RpcRequest request) {
        this.sync = new Sync();
        this.request = request;
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public R get() {

        sync.acquire(1);

        if (response.isError()) {
            Throwable error = response.getError();
            if (error instanceof RpcException){
                throw (RpcException) error;
            }

            if (error instanceof RpcExecuteException){
                throw (RpcExecuteException) error;
            }

            throw new RuntimeException(response.getError());
        }

        return null == response ? null : response.getResult();
    }

    @Override
    public R get(long timeout, TimeUnit unit) throws InterruptedException {
        boolean result = sync.tryAcquireNanos(1, unit.toNanos(timeout));
        if (!result){
            throw new RuntimeException("Timeout exception. Request id: " + this.request.getRequestId()
                    + ". Request class name: " + this.request.getClassName()
                    + ". Request method: " + this.request.getMethodName());
        }

        return null == response ? null : response.getResult();
    }

    public ResponseFuture done(RpcResponse response){
        this.response = response;
        sync.release(1);

        invokeCallback();

        long responseTime = System.currentTimeMillis() - startTime;
        if (responseTime > this.threshold) {
            log.warn("Service response time is too slow. Request id = " + response.getRequestId() + ". Response Time = " + responseTime + "ms");
        }

        return this;
    }

    public ResponseFuture addCallback(ResponseCallback callback) {
        lock.lock();
        try {
            if (isDone()) {
                runCallback(callback);
            } else {
                this.pendingCallbackList.add(callback);
            }
        } finally {
            lock.unlock();
        }

        return this;
    }

    private void invokeCallback(){
        lock.lock();
        try {
            pendingCallbackList.forEach(call -> runCallback(call));
        } finally {
            lock.unlock();
        }
    }

    private void runCallback(ResponseCallback callback){
        CompletableFuture.runAsync(() -> {
            if (response.isError()) {
                callback.fail(response.getError());
            } else {
                callback.success(response.getResult());
            }
        });
    }

    @Override
    public boolean isDone() {
        return sync.isDone();
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isCancelled() {
        throw new UnsupportedOperationException();
    }

    static class Sync extends AbstractQueuedSynchronizer {

        private int pending = 0;
        private int done = 1;

        @Override
        protected boolean tryAcquire(int arg) {
            return getState() == done;
        }

        @Override
        protected boolean tryRelease(int arg) {
            return getState() == pending && this.compareAndSetState(pending, done);
        }

        protected boolean isDone() {
            return getState() == done;
        }

    }

}
