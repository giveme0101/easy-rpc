A easy rpc framework with Netty

## TODO LIST

* [x] [Netty](#netty)
* [x] [多序列化实现](#多序列化实现)
* [x] [心跳检测](#心跳检测)
* [x] [注册中心](#注册中心)
* [x] [负载均衡](#负载均衡)
* [ ] 断线重连
* [ ] SPI

### netty
底层用netty通信

### 多序列化实现
protostuff、hession、kryo

### 心跳检测
客户端定时向服务端发送心跳
服务端连续3次没有收到客户端心跳，剔除该客户端

### 注册中心
自定义适配注册中心： Redis、zookeeper（TODO）

### 负载均衡
默认随机
