# IoT-biz
是一个访问EMQX的MQTT broker范例  

# 代码演示
1）部署一台EMQX，并新增用户认证
2）新建IoT-biz子模块，并引入依赖
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.integration</groupId>
        <artifactId>spring-integration-mqtt</artifactId>
    </dependency>
</dependencies>
```
3）创建MqttProperties、ProducerInfo、ReceiverInfo，加载配置  
4）创建MqttConfiguration用于MQTT配置  
5）配置yaml文件
```yaml
server:
  port: 9983
spring:
  application:
    name: iot-biz-service

# 配置MQTT服务
mqtt:
  urls: tcp://10.156.136.211:1883
#  urls: tcp://192.168.2.201:1883
  username: myeqmx
  password: myeqmx
  cleanSession: false
  producer:
    enabled: true
    client_id: test_producer_1
    topic: linmoo/test/data
  receiver:
    enabled: true
    client_id: test_receiver_1
    topic: linmoo/test/data
#    topic: "$queue/demo/lin/data" # 共享配置
    qos: 1
```
6）新建测试MqttController发送消息

# EMQX的一些说明
> 注意：多个客户端同时共享订阅一个主题，有2种方式  
> 1.$queue开头是采用不带群组的方式  
> 2.$share/g1开头是带群组的方式。  
> 实践的关键的参数设置  
> 1）服务器配置：broker.shared_dispatch_ack_enabled = true，代表如果客户端断开连接，则消息会分到其它服务器（只支持QOS1、QOS2）  
> 2）options.setSessionExpiryInterval(8640L);，代表客户端断开连接，服务器会保存未消费（保存条数有服务器配置）；客户端重新连接则会消费消息  
> 3）按照1）和2）设置，那么在所有客户端奔溃的时候，新发的消息还是会保存在各个客户端队列中，客户端重启时，会继续消费保存在队列的消息。  
> 4）但是有一个注意点，就是存在某个客户端的消息队列，只能这个消息队列的客户端来消费，无法转发到其它客户端  