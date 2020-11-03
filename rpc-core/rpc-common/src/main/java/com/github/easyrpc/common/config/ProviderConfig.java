package com.github.easyrpc.common.config;

import com.github.easyrpc.common.entity.RpcServiceReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name ProviderConfig
 * @Date 2020/11/02 16:38
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProviderConfig {

    /**
     * 注册中心配置
     */
    private String registryAddress;

    /**
     * 序列化协议
     */
    private String serializer;

    /**
     * rpc服务类
     */
    private List<RpcServiceReference> rpcServiceReferences;

    public ProviderConfig addRpcServiceReference(RpcServiceReference rpcServiceReference){

        if (null == rpcServiceReferences){
            rpcServiceReferences = new ArrayList<>();
        }

        rpcServiceReferences.add(rpcServiceReference);

        return this;
    }

}
