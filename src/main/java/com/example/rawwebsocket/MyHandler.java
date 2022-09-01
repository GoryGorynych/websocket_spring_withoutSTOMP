package com.example.rawwebsocket;

import com.google.gson.Gson;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MyHandler extends TextWebSocketHandler {

    private List<UserSessionDTO> userSessions = new CopyOnWriteArrayList<>();

    private Gson gson = new Gson();
    private static final String WEB_USER = "webuser";

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("sessionid = " + session.getId());

        RawMessage inMessage = gson.fromJson(message.getPayload(), RawMessage.class);

        if (inMessage.getType() == MessageType.LOGIN) {
            handleLogin(session, inMessage);
            inMessage.setSessionId(session.getId());
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(gson.toJson(inMessage, RawMessage.class)));
            }
            if (inMessage.getReturnCode() > 1) {
                session.close();
            }
        } else {
            inMessage.setText("OK");
            inMessage.setReturnCode(1);
            validateGeneralMessage(session, inMessage);
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(gson.toJson(inMessage, RawMessage.class)));
            }

        }

    }

    private boolean validateGeneralMessage(WebSocketSession session, RawMessage inMessage) {

        if (inMessage.getUser() == null || inMessage.getUser().isEmpty()) {
            inMessage.setReturnCode(7);
            inMessage.setText("Blank user in inbound message");
            return false;
        }

        if (inMessage.getSessionId() == null || inMessage.getSessionId().isEmpty()) {
            inMessage.setReturnCode(7);
            inMessage.setText("Blank sessionid in inbound message");
            return false;
        }

        if (userSessions.size() == 0) {
            inMessage.setReturnCode(8);
            inMessage.setText(String.format("User %s doesn't logged" + inMessage.getUser()));

            return false;

        } else if (userSessions.size() > 1){
            inMessage.setReturnCode(8);
            inMessage.setText("Multiply sessions in cache");

            return false;
        }
        else {
            UserSessionDTO userSessionDTO = userSessions.get(0);
            if (!userSessionDTO.getUserId().equalsIgnoreCase(inMessage.getUser()) ||
                    !userSessionDTO.getSessionId().equalsIgnoreCase(session.getId())) {
                inMessage.setReturnCode(8);
                inMessage.setText(String.format("Lock by user %s, session %s" + userSessionDTO.getUserId(), userSessionDTO.getSessionId()));

                return false;
            }
        }
        return true;
    }

    private void handleLogin(WebSocketSession session, RawMessage inMessage) {

        if (inMessage.getUser() == null || inMessage.getUser().isEmpty()) {
            inMessage.setReturnCode(7);
            inMessage.setText("Blank user in inbound message");
            return;
        }

        if (userSessions.size() == 0) {

            String sessionId = session.getId();

            UserSessionDTO userSessionDTO = new UserSessionDTO(sessionId, inMessage.getUser());
            userSessions.add(userSessionDTO);

            System.out.println("MyHandler | Added the session:" + session);
            System.out.println("MyHandler | Count of sessions :" + userSessions.size());

            inMessage.setReturnCode(1);
            inMessage.setText(String.format("Session %s added to cache", sessionId));

        } else if (userSessions.size() > 1){
            inMessage.setReturnCode(9);
            inMessage.setText("Multiply sessions in cache");

        }
        else {
            UserSessionDTO userSessionDTO = userSessions.get(0);

            if (!userSessionDTO.getUserId().equalsIgnoreCase(inMessage.getUser()) ||
                    !userSessionDTO.getSessionId().equalsIgnoreCase(session.getId())) {
                inMessage.setReturnCode(9);
                inMessage.setText(String.format("Lock by user %s, session %s", userSessionDTO.getUserId(), userSessionDTO.getSessionId()));

                System.out.println("MyHandler | Count of sessions :" + userSessions.size());
                System.out.println("MyHandler | Lock by user " + userSessionDTO.getUserId() + ", sessionId = " + userSessionDTO.getSessionId());

            } else {
                inMessage.setReturnCode(1);
                inMessage.setText(String.format("User %s already logged", inMessage.getUser()));
            }
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        session.sendMessage(new TextMessage("Connected..."));
        super.afterConnectionEstablished(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//        sessions.remove(session);
        if (userSessions.size() > 0) {
            UserSessionDTO userSessionDTO = new UserSessionDTO(session.getId(), null);
            userSessions.remove(userSessionDTO);

            System.out.println("MyHandler | Removed the session:" + session);
            System.out.println("MyHandler | Count of sessions :" + userSessions.size());
        }

        super.afterConnectionClosed(session, status);

//        throw new Exception("Error epta ");
    }




}
