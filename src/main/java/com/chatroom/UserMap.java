package com.chatroom;

import io.javalin.websocket.WsContext;

import java.util.concurrent.ConcurrentHashMap;

;

public class UserMap extends ConcurrentHashMap<String, UserSession> {

    public void addUser(String sessionId, String username, WsContext context) {
        if (this.contains(sessionId)) {
            return;
        }
        this.put(sessionId, new UserSession(username, context));
    }

    public void removeUser(String sessionId) {
        if (!this.contains(sessionId)) {
            return;
        }
        this.remove(sessionId);
    }
}
