package com.example.websocket_spring_example1;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MyHandler extends TextWebSocketHandler {

    private List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("sessionid = " + session.getId());

        WebSocketMessage<String> response = new TextMessage("response from server: " + message.getPayload());
        try {
            if (session.isOpen()) {
                session.sendMessage(response);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        throw new Exception("Error epta ");

    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        System.out.println("MyHandler | Added the session:" + session);
        super.afterConnectionEstablished(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        System.out.println("MyHandler | Removed the session:" + session);
        super.afterConnectionClosed(session, status);

//        throw new Exception("Error epta ");
    }




}
