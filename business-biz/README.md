# business-biz
1）为了配合openfeign示例(manage-biz-api)，设置的第三方服务接口  
2）为了配合rabbitMQ示例

## 1 rabbitMQ集成
### 1.1 引入common-amqp子模块
```xml
<dependency>
    <groupId>org.example</groupId>
    <artifactId>common-amqp</artifactId>
    <version>${project.version}</version>
</dependency>
```
### 1.2 配置发送者
1）在yaml文件中配置senders
```yaml
senders:  # 本示例配置发送者信息
      test-direct: test-direct
      test-header: test-header
      test-topic: test-topic
      test-fanout: test-fanout
```
2）定义MQController类，直接使用类似以下代码即可发生消息：
```java
TestMessage testMessage = new TestMessage(1,str,new Timestamp((new Date()).getTime()));
rabbitmqSender.send(testMessage, test_direct, "error");
```
### 1.3 配置消费者
1）在yaml文件中配置receiver
```yaml
receivers: # 本示例配置消费者信息
      test-direct-queue:
        queueName: test-direct-queue  # 队列名称
        exchange: test-direct  # 队列绑定交换机
        exchangeType: DIRECT   # 交换机类型DIRECT、HEADER、TOPIC、FANOUT
        routingKey: error, info  # routingKey，只有DIRECT和TOPIC有效
        acknowledgeMode: MANUAL  # 手动还是自动确认
```
2）定义TestXXXReceiverService类，直接使用类似以下代码即可发生消息：
```java
/**
 * Direct消息消费者，同时手动确认模式
 */
@RabbitListener(queues="${spring.rabbitmq.receivers.test-direct-queue.queueName}"
        , ackMode = "${spring.rabbitmq.receivers.test-direct-queue.acknowledgeMode}")
public void receive(TestMessage testMessage, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
    log.info("handleDirectMessage:{}", JSON.toJSONString(testMessage));
    channel.basicAck(tag,true);
}
```

# 2 minIO对象存储集成
1）引入common-oss子模块
```xml
<dependency>
    <groupId>org.example</groupId>
    <artifactId>common-oss</artifactId>
    <version>${project.version}</version>
</dependency>
```
2）在yaml文件配置以下信息
```yaml
oss:
  enabled: true  # 是否开启oss存储配置
  api: true  # 自动开启API接口
  endpoint: http://127.0.0.1:9005  # MinIO服务器的URL
  access-key: minioadmin      # 访问密钥
  secret-key: minioadmin      # 密钥密码
  bucket-name: works     # 默认的Bucket名称
```
3）直接就可以使用OssEndpoint的接口操作oss
