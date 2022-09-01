package com.example.rawwebsocket;

public class RawMessage {

    private MessageType type;
    private String user;
    private String text;
    private String sessionId;

    private int returnCode;

    public RawMessage(MessageType type, String user, String text, String sessionId) {
        this.type = type;
        this.user = user;
        this.text = text;
        this.sessionId = sessionId;
    }

    public int getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
