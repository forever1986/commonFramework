package com.demo.iot.mqtt.local;

import org.eclipse.paho.mqttv5.client.*;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;


public class Consumer2 {

    private static final String BROKER_URL = "tcp://192.168.2.201:1883";
    /**
     * 注意：多个客户端同时共享订阅一个主题，有2种方式
     * 1.$queue开头是采用不带群组的方式
     * 2.$share/g1开头是带群组的方式。
     * 实践的关键的参数设置
     * 1）服务器配置：broker.shared_dispatch_ack_enabled = true，代表如果客户端断开连接，则消息会分到其它服务器（只支持QOS1、QOS2）
     * 2）options.setSessionExpiryInterval(8640L);，代表客户端断开连接，服务器会保存未消费（保存条数有服务器配置）；客户端重新连接则会消费消息
     * 3）按照1）和2）设置，那么在所有客户端奔溃的时候，新发的消息还是会保存在各个客户端队列中，客户端重启时，会继续消费保存在队列的消息。
     * 4）但是有一个注意点，就是存在某个客户端的消息队列，只能这个消息队列的客户端来消费，无法转发到其它客户端
     *
     */
    private static final String TOPIC = "$queue/demo/lin/data";

    private MqttClient consumerClient2;
    private MqttConnectionOptions options;

    public static void main(String[] args) {
        Consumer2 test = new Consumer2();
        try {
            test.connectAndTest();
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    public void connectAndTest() throws MqttException {
        consumerClient2 = new MqttClient(BROKER_URL, "consumer2-client");

        try {
            // 设置连接选项
            options = new MqttConnectionOptions();
            options.setCleanStart(false);
            options.setSessionExpiryInterval(8640L);
            // 连接到MQTT代理服务器
            consumerClient2.connect(options);
            // 创建回调接口
            MqttCallback callback = new MqttCallback() {
                @Override
                public void disconnected(MqttDisconnectResponse mqttDisconnectResponse) {
                    System.out.println("Connection lost: " + mqttDisconnectResponse.getReasonString());
                }
                @Override
                public void mqttErrorOccurred(MqttException e) {
                    System.out.println("MQTT error occurred: " + e.getMessage());
                }
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    System.out.println("Received message: " + new String(message.getPayload()) + " on topic: " + topic);
                }
                @Override
                public void deliveryComplete(IMqttToken iMqttToken) {
                    System.out.println("MQTT deliveryComplete");
                }
                @Override
                public void connectComplete(boolean b, String s) {
                    System.out.println("Connected to MQTT broker: " + s);
                    // 订阅主题
                    try {
                        consumerClient2.subscribe(TOPIC,1);
                    } catch (MqttException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Subscribed to topic: " + TOPIC);
                }

                @Override
                public void authPacketArrived(int i, MqttProperties mqttProperties) {

                }

            };

            // 设置回调
            consumerClient2.setCallback(callback);
            // 取消订阅
            consumerClient2.unsubscribe(TOPIC);
            System.out.println("Unsubscribed from topic: " + TOPIC);
            // 订阅主题
            consumerClient2.subscribe(TOPIC,1);
            System.out.println("Subscribed to topic: " + TOPIC);
            //启动状态监控线程
            Thread statusMonitorThread = new Thread(this::monitorClientStatus);
            statusMonitorThread.start();

            //主线程将一直运行，保持应用程序活跃
            while (true) {
                // 这里可以执行其他任务，或者简单地休眠
                Thread.sleep(60000);
                //sampleClient.disconnectForcibly(2);
                System.out.println("close client");
                consumerClient2.close(true);
                break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 确保客户端关闭
            if (consumerClient2 != null && consumerClient2.isConnected()) {
                try {
                    consumerClient2.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void monitorClientStatus() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                System.out.println("MQTT client status: " + consumerClient2.isConnected());
                // 检查MQTT客户端是否连接
                if (!consumerClient2.isConnected()) {
                    System.out.println("MQTT client is not connected, attempting to reconnect.");
                    // 尝试重新连接
                    try {
                        consumerClient2.connect(options);
                    } catch (MqttException e) {
                        System.out.println("Failed to reconnect: " + e.getMessage());
                        // 可以选择在这里处理重连失败的情况，例如重试或退出
                    }
                }
                // 适当休眠，避免CPU过载
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                // 线程被中断，退出循环
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                System.out.println("Error while monitoring MQTT client status: " + e.getMessage());
            }
        }
    }
}
