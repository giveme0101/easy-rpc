package com.github.easyrpc.common.context;

import com.github.easyrpc.common.entity.ContextEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.Observable;
import java.util.Observer;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name ObservableContext
 * @Date 2020/11/02 11:50
 */
@Slf4j
public abstract class ObservableContext extends Observable implements IContext {

    @Override
    public void publish(ContextEvent event) {
        log.debug("publish event: {}", event);
        this.setChanged();
        this.notifyObservers(event);
    }

    @Override
    public void addListener(Observer listener) {
        log.debug("add event listener: {}", listener);
        this.addObserver(listener);
    }

    @Override
    public void deleteListener(Observer listener) {
        log.debug("delete event listener: {}", listener);
        this.deleteObserver(listener);
    }

}
