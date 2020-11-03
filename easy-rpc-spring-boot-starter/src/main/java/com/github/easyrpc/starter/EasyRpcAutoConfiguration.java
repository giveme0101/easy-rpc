package com.github.easyrpc.starter;

import com.github.easyrpc.common.entity.RpcServiceReference;
import com.github.easyrpc.core.consumer.context.NettyClientContext;
import com.github.easyrpc.core.provider.ProviderConfig;
import com.github.easyrpc.core.provider.context.NettyServerContext;
import com.github.easyrpc.starter.consumer.RpcReferenceBeanPostProcessor;
import com.github.easyrpc.starter.provider.EasyRpcProperties;
import com.github.easyrpc.starter.provider.RpcService;
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
            config.addRpcServiceReference(generateReference(next.getValue()));
        }

        return new NettyServerContext(config);
    }

    @Bean(destroyMethod = "close")
    public NettyClientContext nettyClientContext(EasyRpcProperties properties){

        ProviderConfig config = new ProviderConfig();
        config.setRegistryAddress(properties.getRegistryAddress());
        config.setSerializer(properties.getSerializer());

        return new NettyClientContext(config);
    }

    private RpcServiceReference generateReference(Object instance) {

        Class<?> aClass = instance.getClass();
        Class<?>[] interfaces = aClass.getInterfaces();

        RpcService annotation = aClass.getAnnotation(RpcService.class);
        String serviceName = annotation.serviceName();
        String version = annotation.version();

        return RpcServiceReference.builder()
                .serviceName(StringUtils.isEmpty(serviceName) ? aClass.getName() : serviceName)
                .version(version)
                .instance(instance)
                .interfaceClass(interfaces[0])
                .build();
    }

}
