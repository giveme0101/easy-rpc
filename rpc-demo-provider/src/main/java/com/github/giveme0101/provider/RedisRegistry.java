package com.github.giveme0101.provider;

import com.github.giveme0101.RedisRegister;
import com.github.giveme0101.rpc.core.provider.context.EventEnum;
import com.github.giveme0101.rpc.core.provider.context.ServiceProvider;
import com.github.giveme0101.rpc.core.provider.util.LocalAddressHolder;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.net.InetSocketAddress;
import java.util.Observable;
import java.util.Observer;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name RedisRegistry
 * @Date 2020/09/21 14:32
 */
@Slf4j
public class RedisRegistry implements Observer {

    private RedisRegister register;

    public RedisRegistry(String host, String pwd, int port) {
        register = RedisRegister.getInstance(host, port, pwd);
    }

    @Override
    public void update(Observable o, Object arg) {

        EventEnum event = (EventEnum) arg;
        val serviceProvider = (ServiceProvider) o;

        if (EventEnum.SERVER_STARTING.equals(event)){
            serviceProvider.getServiceMap().keySet().forEach((String interfaceName) -> {
                InetSocketAddress socketAddress = new InetSocketAddress(LocalAddressHolder.HOST, LocalAddressHolder.PORT);
                register.registerProvider(interfaceName, socketAddress);
                log.info("服务注册成功: {}", interfaceName);
            });

        }

        if (EventEnum.STOP.equals(event)){
            serviceProvider.getServiceMap().keySet().forEach((String interfaceName) -> {
                InetSocketAddress socketAddress = new InetSocketAddress(LocalAddressHolder.HOST, LocalAddressHolder.PORT);
                register.unregisterProvider(interfaceName, socketAddress);
                log.info("移除服务注册: {}", interfaceName);
            });
        }

    }

}
