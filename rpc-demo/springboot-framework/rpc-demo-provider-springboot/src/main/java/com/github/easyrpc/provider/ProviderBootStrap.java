package com.github.easyrpc.provider;

import com.github.easyrpc.starter.provider.RpcScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name ProviderBootStrap
 * @Date 2020/10/29 10:59
 */
@SpringBootApplication
@RpcScan(basePackages = {"com.github.easyrpc.provider.rpc"})
public class ProviderBootStrap {

    public static void main(String[] args) {
        SpringApplication.run(ProviderBootStrap.class);
    }

}
