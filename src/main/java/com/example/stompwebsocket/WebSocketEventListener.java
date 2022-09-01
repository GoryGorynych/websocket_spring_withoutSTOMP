package com.example.stompwebsocket;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {


    private SimpMessagingTemplate messagingTemplate;


    public WebSocketEventListener(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
//        this.participantRepository = participantRepository;
    }

    @EventListener
    private void handleSessionConnected(SessionConnectEvent event) {
        System.out.println("debug");

        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
//        String username = headers.getUser().getName();
//        String username = "vasya";

//        OutputMessage loginEvent = new OutputMessage(username);
//        messagingTemplate.convertAndSend("/topic/messages", "username = " + username );

        // We store the session as we need to be idempotent in the disconnect event processing
//        participantRepository.add(headers.getSessionId(), loginEvent);
    }

    @EventListener
    private void handleSessionDisconnect(SessionDisconnectEvent event) {
        System.out.println("debug");
    }

}
