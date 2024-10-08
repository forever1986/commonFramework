package com.demo.iot.websocket;

import javax.websocket.*;

@ClientEndpoint
public class SocketClient {

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("WebSocket opened: " + session.getId());
        sendMessage(session, "Hello, WebSocket Server!");
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        System.out.println("Message from server: " + message);
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        System.out.println("WebSocket closed: " + session.getId() + ", Reason: " + reason.getReasonPhrase());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.out.println("WebSocket error: " + throwable.getMessage());
    }

    public void sendMessage(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SocketClient s = new SocketClient();

    }
}
