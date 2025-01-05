package com.chatroom;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class UserMap extends ConcurrentHashMap<String, String> {
    public void addUser(String username, String sessionId) {
        if (this.contains(sessionId)) {
            return;
        }
        this.put(sessionId, username);
    }

    public void removeUser(String sessionId) {
        if (!this.contains(sessionId)) {
            return;
        }
        this.remove(sessionId);
    }

    public Collection<String> getUsers() {
        return this.values();
    }
}
