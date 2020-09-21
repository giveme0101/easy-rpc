package com.github.giveme0101.rpc.core.common;

public interface IContext {

    void init();

    void start();

    void stop();

    default void refresh(){
        stop();
        start();
    }

}
