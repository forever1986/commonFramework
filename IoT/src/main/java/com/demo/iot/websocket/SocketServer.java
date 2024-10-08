//package com.demo.websocket;
//
//import org.java_websocket.WebSocket;
//import org.java_websocket.handshake.ClientHandshake;
//import org.java_websocket.server.WebSocketServer;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.InetSocketAddress;
//import java.net.UnknownHostException;
//
//public class SocketServer extends WebSocketServer {
//
//    public SocketServer(int port) throws UnknownHostException {
//        super(new InetSocketAddress(port));
//    }
//
//    public SocketServer(InetSocketAddress address) {
//        super(address);
//    }
//
//    @Override
//    public void onOpen(WebSocket conn, ClientHandshake handshake) {
//        conn.send("Welcome to the server!"); // This method sends a message to the new client
//        broadcast("new connection: " + handshake
//                .getResourceDescriptor()); // This method sends a message to all clients connected
//        System.out.println(
//                conn.getRemoteSocketAddress().getAddress().getHostAddress() + " entered the room!");
//
//    }
//
//    @Override
//    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
//        broadcast(conn + " has left the room!");
//        System.out.println(conn + " has left the room!");
//
//    }
//
//    @Override
//    public void onMessage(WebSocket conn, String message) {
//
//        broadcast(message);
//        System.out.println(conn + ": " + message);
//    }
//
//    @Override
//    public void onError(WebSocket conn, Exception ex) {
//        ex.printStackTrace();
//        if (conn != null) {
//            // some errors like port binding failed may not be assignable to a specific
//            // websocket
//        }
//
//    }
//
//    @Override
//    public void onStart() {
//        System.out.println("Server started!");
//        setConnectionLostTimeout(0);
//        setConnectionLostTimeout(100);
//
//    }
//
//    public static void main(String[] args) throws InterruptedException, IOException {
////        InetSocketAddress address = new InetSocketAddress("f75d1379.ala.dedicated.aliyun.emqxcloud.cn", 8083);
////        PublisherSocketServer s = new PublisherSocketServer(address);
//        SocketServer s = new SocketServer(8083);
//        s.start();
//        System.out.println("ChatServer started on port: " + s.getPort());
//
//        BufferedReader sysin = new BufferedReader(new InputStreamReader(System.in));
//        while (true) {
//            String in = sysin.readLine();
//            s.broadcast(in);
//            if (in.equals("exit")) {
//                s.stop(1000);
//                break;
//            }
//        }
//    }
//
//}
