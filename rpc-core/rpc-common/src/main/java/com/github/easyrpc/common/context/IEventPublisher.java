package com.github.easyrpc.common.context;

import com.github.easyrpc.common.entity.ContextEvent;

import java.util.Observer;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name EventPublisher
 * @Date 2020/10/30 11:26
 */
public interface IEventPublisher {

    void publish(ContextEvent event);

    void addListener(Observer listener);

    void deleteListener(Observer listener);

}
