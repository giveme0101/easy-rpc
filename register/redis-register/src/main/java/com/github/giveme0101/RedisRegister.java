package com.github.giveme0101;

import com.github.easyrpc.common.loadbalance.LoadBalance;
import com.github.easyrpc.common.register.ProviderRegister;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.net.InetSocketAddress;
import java.util.Set;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name RedisRegister
 * @Date 2020/09/21 10:49
 */
@Slf4j
public class RedisRegister implements ProviderRegister {

    private String host = "127.0.0.1";
    private Integer port = 6379;
    private String password = "";
    private Jedis jedis;

    @Override
    public void registerProvider(String rpcServiceName, InetSocketAddress socketAddress) {
        String host = socketAddress.getAddress().getHostAddress() + ":" + socketAddress.getPort();
        jedis.sadd(rpcServiceName, host);
    }

    @Override
    public void unregisterProvider(String rpcServiceName, InetSocketAddress socketAddress) {
        String host = socketAddress.getAddress().getHostAddress() + ":" + socketAddress.getPort();
        jedis.srem(rpcServiceName, host);
    }

    @Override
    public InetSocketAddress lookupProvider(String nozzle) {
        String member = jedis.srandmember(nozzle);
        if (null == member || member.isEmpty()){
            throw new RuntimeException("获取服务失败！");
        }

        String[] socketAddressArray = member.split(":");
        String host = socketAddressArray[0];
        int port = Integer.parseInt(socketAddressArray[1]);
        return new InetSocketAddress(host, port);
    }

    @Override
    public InetSocketAddress lookupProvider(String nozzle, LoadBalance balance) {
        Set<String> members = jedis.smembers(nozzle);
        String member = balance.select(members);
        if (null == member || member.isEmpty()){
            throw new RuntimeException("获取服务失败！");
        }

        String[] socketAddressArray = member.split(":");
        String host = socketAddressArray[0];
        int port = Integer.parseInt(socketAddressArray[1]);
        return InetSocketAddress.createUnresolved(host, port);
    }

    public RedisRegister(String host, int port, String password){
        this.host = host;
        this.port = port;
        this.password = password;
        this.init();
    }

    private void init(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(5);
        config.setTestOnBorrow(false);
        JedisPool jedisPool = new JedisPool(config, host, port, 2000, password, 1);
        jedis = jedisPool.getResource();
        log.info("redis registry connected at {}:{}", host, port);
    }

}
