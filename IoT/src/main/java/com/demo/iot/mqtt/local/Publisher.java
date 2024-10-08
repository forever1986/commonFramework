package com.demo.iot.mqtt.local;

import org.eclipse.paho.mqttv5.client.*;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;

import java.nio.charset.StandardCharsets;

public class Publisher {


    private static final String BROKER_URL = "tcp://192.168.2.201:1883";
    private static final String TOPIC = "demo/lin/data";

    private MqttClient publishClient;
    MemoryPersistence persistence = new MemoryPersistence();

    public static void main(String[] args) {
        Publisher test = new Publisher();
        try {
            test.connectAndTest();
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    public void connectAndTest() throws MqttException {
        publishClient = new MqttClient(BROKER_URL, "publish-client", persistence);

        try {
            // 设置连接选项
            MqttConnectionOptions options = new MqttConnectionOptions();
            options.setCleanStart(false);
            // 连接到MQTT代理服务器
            publishClient.connect(options);
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
                }

                @Override
                public void authPacketArrived(int i, MqttProperties mqttProperties) {

                }

            };

            // 设置回调
            publishClient.setCallback(callback);
            String message = "{\"id\": 6 , \"name\": \"test6\"}";
            publishClient.publish(TOPIC, message.getBytes(StandardCharsets.UTF_8),1,false );
            publishClient.disconnect();
            publishClient.close(true);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 确保客户端关闭
            if (publishClient != null && publishClient.isConnected()) {
                try {
                    publishClient.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
