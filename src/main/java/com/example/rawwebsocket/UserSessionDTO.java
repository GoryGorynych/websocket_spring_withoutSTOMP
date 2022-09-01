package com.example.rawwebsocket;

import java.util.Objects;

public class UserSessionDTO {

    private String sessionId;
    private String userId;

    public UserSessionDTO(String sessionId, String userId) {
        this.sessionId = sessionId;
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserSessionDTO that = (UserSessionDTO) o;
        return sessionId.equals(that.sessionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionId);
    }

    @Override
    public String toString() {
        return "UserSessionDTO{" +
                "sessionId='" + sessionId + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
