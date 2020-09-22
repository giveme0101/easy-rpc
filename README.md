A easy rpc framework with Netty

## TODO LIST

* [x] [多序列化实现](#多序列化实现)
* [x] [心跳检测](#心跳检测)
* [x] [注册中心](#注册中心)
* [x] [负载均衡](#负载均衡)
* [ ] 断线重连
* [ ] SPI

### 多序列化实现
protostuff、hession、kryo

### 心跳检测

### 注册中心
使用redis做注册中心，[X] 注册中心耦合度过高，无法自定义

### 负载均衡
默认随机