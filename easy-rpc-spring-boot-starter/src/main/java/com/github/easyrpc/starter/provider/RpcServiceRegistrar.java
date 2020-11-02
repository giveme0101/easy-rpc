package com.github.easyrpc.starter.provider;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name RpcServiceRegistrar
 * @Date 2020/10/29 11:21
 */
public class RpcServiceRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {

    private ResourceLoader resourceLoader;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) {

        AnnotationAttributes attributes = AnnotationAttributes.fromMap(
                annotationMetadata.getAnnotationAttributes(RpcScan.class.getName())
        );
        if (null == attributes){
            return;
        }

        String[] scanBasePackages = attributes.getStringArray("basePackages");
        String[] basePackages = Arrays.stream(scanBasePackages)
                .filter(StringUtils::hasText)
                .collect(Collectors.toList())
                .toArray(new String[0]);
        if (basePackages == null || basePackages.length < 1){
            return;
        }

        RpcServiceScanner scanner = new RpcServiceScanner(registry, false);
        Optional.ofNullable(resourceLoader).ifPresent(rl -> scanner.setResourceLoader(rl));

        scanner.doScan(basePackages);
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

}