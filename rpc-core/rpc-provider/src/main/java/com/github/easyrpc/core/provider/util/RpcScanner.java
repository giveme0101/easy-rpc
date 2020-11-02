package com.github.easyrpc.core.provider.util;

import com.github.easyrpc.core.provider.RpcService;
import com.github.easyrpc.common.entity.RpcServiceReference;
import com.github.easyrpc.common.util.Assert;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name RpcScanner
 * @Date 2020/09/27 16:14
 */
@Slf4j
public class RpcScanner {

    public static List<RpcServiceReference> scanRpcService(String basePackage) {

        Assert.notNull(basePackage, "missing scan basePackage");

        List<Class> scanList = ClassScanner.scan(basePackage, (Class clazz) -> {
            return clazz.isAnnotationPresent(RpcService.class);
        });

        List<RpcServiceReference> referenceList = new ArrayList<>();

        for (final Class aClass : scanList) {

            RpcService rpcService = (RpcService) aClass.getAnnotation(RpcService.class);
            Class[] interfaces = aClass.getInterfaces();
            for (final Class anInterface : interfaces) {

                RpcServiceReference rpcServiceProperties = RpcServiceReference.builder()
                        .serviceName(anInterface.getName())
                        .interfaceClass(anInterface)
                        .implClass(aClass)
                        .version(rpcService.version())
                        .build();
                referenceList.add(rpcServiceProperties);
            }
        }

        return referenceList;
    }

}
