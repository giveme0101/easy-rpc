package com.github.easyrpc.core.provider;

import com.github.easyrpc.common.config.AppConfig;
import com.github.easyrpc.common.entity.RpcServiceReference;
import lombok.*;

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
public class ProviderConfig extends AppConfig {

    /**
     * rpc服务类
     */
    private List<RpcServiceReference> rpcServiceReferences;

    public AppConfig addRpcServiceReference(RpcServiceReference rpcServiceReference){

        if (null == rpcServiceReferences){
            rpcServiceReferences = new ArrayList<>();
        }

        rpcServiceReferences.add(rpcServiceReference);

        return this;
    }

}
