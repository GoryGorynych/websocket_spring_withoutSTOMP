package com.example.rawwebsocket;

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
    private List<UserSessionDTO> userSessions = new CopyOnWriteArrayList<>();

    private static final String WEB_USER = "webuser";

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
        if (userSessions.size() > 0) {
            UserSessionDTO userSessionDTO = userSessions.get(0);
            session.sendMessage(new TextMessage("Lock by user " + userSessionDTO.getUserId()));
            System.out.println("MyHandler | Count of sessions :" + sessions.size());
            System.out.println("MyHandler | Lock by user " + userSessionDTO.getUserId() + ", sessionId = " + userSessionDTO.getSessionId());
            session.close();
        } else {
            UserSessionDTO userSessionDTO = new UserSessionDTO(session.getId(), WEB_USER);
            userSessions.add(userSessionDTO);

            System.out.println("MyHandler | Added the session:" + session);
            System.out.println("MyHandler | Count of sessions :" + sessions.size());

            session.sendMessage(new TextMessage(userSessionDTO.toString()));
            super.afterConnectionEstablished(session);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//        sessions.remove(session);
        UserSessionDTO userSessionDTO = new UserSessionDTO(session.getId(), WEB_USER);
        userSessions.remove(userSessionDTO);

        System.out.println("MyHandler | Removed the session:" + session);
        System.out.println("MyHandler | Count of sessions :" + sessions.size());
        super.afterConnectionClosed(session, status);

//        throw new Exception("Error epta ");
    }




}
