package com.github.easyrpc.starter.consumer;

import com.github.easyrpc.common.util.ReflectionUtils;
import com.github.easyrpc.core.provider.context.NettyServerContext;
import com.github.easyrpc.starter.RpcReference;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Field;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name RpcReferenceBeanPostProcessor
 * @Date 2020/11/02 17:31
 */
public class RpcReferenceBeanPostProcessor extends InstantiationAwareBeanPostProcessorAdapter implements ApplicationContextAware {

    private NettyServerContext nettyClientContext;

    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {

        Field[] declaredFields = bean.getClass().getDeclaredFields();
        for (final Field field : declaredFields) {
            if (field.isAnnotationPresent(RpcReference.class)){

                RpcReference annotation = field.getAnnotation(RpcReference.class);
                String serviceName = annotation.serviceName();
                String version = annotation.version();
                Class<?> type = field.getType();

                Object client = nettyClientContext.getClient(type, serviceName, version);

                ReflectionUtils.makeAccessible(field);
                try {
                    field.set(bean, client);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return pvs;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        nettyClientContext = applicationContext.getBean(NettyServerContext.class);
    }
}
