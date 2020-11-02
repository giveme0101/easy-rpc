package com.github.easyrpc.core.provider.context;

import com.github.easyrpc.common.entity.ContextEvent;
import com.github.easyrpc.common.register.ProviderRegister;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.Observable;
import java.util.Observer;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name RegistrySupport
 * @Date 2020/09/21 14:32
 */
@Slf4j
public class RegistrySupport implements Observer {

    private ProviderRegister register;

    public RegistrySupport(ProviderRegister register) {
        this.register = register;
    }

    @Override
    public void update(Observable o, Object arg) {

        ContextEvent event = (ContextEvent) arg;
        val serviceProvider = (ServiceProvider) o;

//        if (ContextEvent.SERVER_STARTING.equals(event)){
//            serviceProvider.getServiceMap().keySet().forEach((String interfaceName) -> {
//                InetSocketAddress socketAddress = new InetSocketAddress(LocalAddressHolder.HOST, LocalAddressHolder.PORT);
//                register.registerProvider(interfaceName, socketAddress);
//                log.info("服务注册成功: {}", interfaceName);
//            });
//        }
//
//        if (ContextEvent.STOP.equals(event)){
//            serviceProvider.getServiceMap().keySet().forEach((String interfaceName) -> {
//                InetSocketAddress socketAddress = new InetSocketAddress(LocalAddressHolder.HOST, LocalAddressHolder.PORT);
//                register.unregisterProvider(interfaceName, socketAddress);
//                log.info("移除服务注册: {}", interfaceName);
//            });
//        }
    }

}