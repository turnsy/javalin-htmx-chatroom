package com.chatroom;

import io.javalin.websocket.WsContext;

public record UserSession(String username, WsContext context) {}
