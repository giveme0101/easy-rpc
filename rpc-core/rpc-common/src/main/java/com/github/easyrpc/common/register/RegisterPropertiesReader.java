package com.github.easyrpc.common.register;

import com.github.easyrpc.common.config.ConfigReader;
import com.github.easyrpc.common.constant.AppConstants;
import com.github.easyrpc.common.util.Assert;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name RegisterPropertiesReader
 * @Date 2020/11/02 13:46
 */
public class RegisterPropertiesReader {

    public static RegisterProperties read(ConfigReader reader){

        Assert.notNull(reader, "config reader is null");

        String host = reader.getValue(AppConstants.K_REGISTRY_HOST, "127.0.0.1");
        String port = reader.getValue(AppConstants.K_REGISTRY_PORT, "6379");
        String pwd = reader.getValue(AppConstants.K_REGISTRY_PWD, "");

        try {
            return RegisterProperties.builder()
                    .host(host)
                    .port(Integer.valueOf(port))
                    .password(pwd)
                    .build();
        } catch (NumberFormatException exception){
            throw new RuntimeException("port error: " + port);
        }
    }

}
