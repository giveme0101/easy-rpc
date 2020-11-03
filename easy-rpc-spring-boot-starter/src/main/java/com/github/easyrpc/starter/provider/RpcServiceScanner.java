package com.github.easyrpc.starter.provider;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.Set;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name RpcServiceScanner
 * @Date 2020/10/29 11:24
 */
public class RpcServiceScanner extends ClassPathBeanDefinitionScanner {

    public RpcServiceScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters) {
        super(registry, useDefaultFilters);
    }

    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        addIncludeFilter(new AnnotationTypeFilter(RpcService.class));
        return super.doScan(basePackages);
    }

}
