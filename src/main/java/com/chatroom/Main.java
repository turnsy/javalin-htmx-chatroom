package com.chatroom;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.staticfiles.Location;
import io.javalin.rendering.FileRenderer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // global maps used
        UserMap userMap = new UserMap();
        ObjectMapper messageMapper = new ObjectMapper();
        HTMLFactory HtmlFactory = new HTMLFactory();

        var app = Javalin.create(config -> {
        });
        app.get("/", ctx -> {
            ctx.html(HtmlFactory.getMainHTML());
        });

        app.ws("/chat-socket", ws -> {
            ws.onMessage(ctx -> {
                Message currentMessage = messageMapper.readValue(ctx.message(), Message.class);
                switch (currentMessage.getType()) {
                    case Message.Type.username -> {
                        userMap.addUser(ctx.sessionId(), currentMessage.content, ctx);
                    }
                    case Message.Type.message -> {
                        for(String session: userMap.keySet()) {
                            UserSession userSession = userMap.get(session);
                            userSession.context().send(HtmlFactory.getMessageHTML(currentMessage.content, userSession.username(), session.equals(ctx.sessionId())));
                        }
                    }
                }
            });
            ws.onClose(ctx -> {
                userMap.removeUser(ctx.sessionId());
            });
        });
        app.start(7070);
    }
}