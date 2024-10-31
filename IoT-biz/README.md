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
