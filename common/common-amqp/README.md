# common-qmqp
是一个公共包，主要是自动配置RabbitMQ消息队列。

为了方便使用rabbitmq，将其一些生产者、队列绑定通过RabbitmqConfig进行配置  
1）该子模块以spring.factories方式发布
2）定义RabbitmqSender封装生产者的配置
3）定义ConfigurationProperties和QueueInfo读取receivers消费者的配置信息（配置在yaml文件中）  
4）在RabbitmqConfig中注册队列并绑定队列到交换机，其配置在yaml文件中说明
```yaml
receivers: # 本示例配置消费者信息
      test-direct-queue:
        queueName: test-direct-queue  # 队列名称
        exchange: test-direct  # 队列绑定交换机
        exchangeType: DIRECT   # 交换机类型DIRECT、HEADER、TOPIC、FANOUT
        routingKey: error, info  # routingKey，只有DIRECT和TOPIC有效
        acknowledgeMode: MANUAL  # 手动还是自动确认
```
5）子模块business-biz引用该子模块  
6）配置生产者：在yaml文件中配置senders，同时直接使用类似以下代码即可发生消息：
```java
TestMessage testMessage = new TestMessage(1,str,new Timestamp((new Date()).getTime()));
rabbitmqSender.send(testMessage, test_direct, "error");
```
7）配置消费者：在yaml文件中配置receiver，同时直接使用类似以下代码即可发生消息：
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