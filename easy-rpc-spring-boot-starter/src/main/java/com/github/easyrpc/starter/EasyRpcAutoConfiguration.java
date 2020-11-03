package com.github.easyrpc.starter;

import com.github.easyrpc.common.config.ProviderConfig;
import com.github.easyrpc.common.entity.RpcServiceReference;
import com.github.easyrpc.core.provider.context.NettyServerContext;
import com.github.easyrpc.starter.consumer.RpcReferenceBeanPostProcessor;
import com.github.easyrpc.starter.provider.EasyRpcProperties;
import com.github.easyrpc.starter.provider.RpcService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Iterator;
import java.util.Map;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name EasyRpcAutoConfiguration
 * @Date 2020/11/02 16:53
 */
@Slf4j
@Configuration
@Import(RpcReferenceBeanPostProcessor.class)
@EnableConfigurationProperties(value = EasyRpcProperties.class)
public class EasyRpcAutoConfiguration {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean(initMethod = "run", destroyMethod = "close")
    public NettyServerContext nettyServerContext(EasyRpcProperties properties){

        ProviderConfig config = new ProviderConfig();
        config.setRegistryAddress(properties.getRegistryAddress());
        config.setSerializer(properties.getSerializer());

        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(RpcService.class);
        Iterator<Map.Entry<String, Object>> iterator = beansWithAnnotation.entrySet().iterator();
        while (iterator.hasNext()) {

            Map.Entry<String, Object> next = iterator.next();
            Object instance = next.getValue();

            Class<?> aClass = instance.getClass();
            Class<?>[] interfaces = aClass.getInterfaces();
            if (interfaces.length != 1) {
                log.warn("{} have more than one interface ", instance.getClass());
                continue;
            }

            RpcService annotation = aClass.getAnnotation(RpcService.class);
            String serviceName = annotation.serviceName();

            config.addRpcServiceReference(RpcServiceReference.builder()
                    .serviceName(StringUtils.isEmpty(serviceName) ? aClass.getName() : serviceName)
                    .version(annotation.version())
                    .instance(instance)
                    .interfaceClass(interfaces[0])
                    .build());
        }

        return new NettyServerContext(config);
    }

}
