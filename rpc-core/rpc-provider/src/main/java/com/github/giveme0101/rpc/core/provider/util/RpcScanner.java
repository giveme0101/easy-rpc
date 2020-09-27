package com.github.giveme0101.rpc.core.provider.util;

import com.github.giveme0101.rpc.core.common.RpcService;
import com.github.giveme0101.rpc.core.common.entity.RpcServiceProperties;
import com.github.giveme0101.rpc.core.common.util.SingletonFactory;
import com.github.giveme0101.rpc.core.provider.context.ServiceProvider;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name RpcScanner
 * @Date 2020/09/27 16:14
 */
@Slf4j
public class RpcScanner {

    public static void scanRpcService(String basePackage, ServiceProvider serviceProvider) {

        if (null == basePackage){
            throw new NullPointerException("missing or invalid config properties: service.impl.package");
        }

        List<Class> clazzList = Scanner.scan(basePackage);
        clazzList.stream()
                .filter(clz -> clz.isAnnotationPresent(RpcService.class))
                .forEach(impl -> {
                    RpcService rpcService = (RpcService) impl.getAnnotation(RpcService.class);
                    Class[] interfaces = impl.getInterfaces();
                    for (final Class anInterface : interfaces) {

                        RpcServiceProperties rpcServiceProperties = RpcServiceProperties.builder()
                                .serviceName(anInterface.getName())
                                .version(rpcService.version())
                                .build();
                        Object instance = SingletonFactory.getInstance(impl);
                        serviceProvider.addService(instance, rpcServiceProperties);
                        log.info("scan impl {} -> {}", anInterface.getName(), impl);

                    }
                });
    }

}
