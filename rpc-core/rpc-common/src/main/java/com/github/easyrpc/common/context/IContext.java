package com.github.easyrpc.common.context;

public interface IContext extends IEventPublisher {

    void run();

    void close();

}
