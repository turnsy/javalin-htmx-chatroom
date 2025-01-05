package com.chatroom;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.staticfiles.Location;
import io.javalin.rendering.FileRenderer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
    public static void main(String[] args) {
        // global maps used
        UserMap userMap = new UserMap();
        ObjectMapper messageMapper = new ObjectMapper();
        HTMLFactory HtmlFactory = new HTMLFactory();

        var app = Javalin.create(config -> {
            config.staticFiles.add("src/main/resources/public", Location.EXTERNAL);
            config.fileRenderer(new FileRenderer() {
                @NotNull
                @Override
                public String render(@NotNull String s, @NotNull Map<String, ?> map, @NotNull Context context) {
                    try {
                        return new String(getClass().getResourceAsStream(s).readAllBytes());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        });
        app.get("/", ctx -> {
            ctx.render("/public/index.html");
        });

        app.ws("/chat-socket", ws -> {
            ws.onMessage(ctx -> {
                Message currentMessage = messageMapper.readValue(ctx.message(), Message.class);
                switch(currentMessage.getType()) {
                    case Message.Type.username -> {
                        userMap.addUser(currentMessage.getContent(), ctx.sessionId());
                        ctx.send(HtmlFactory.getUsersHTML(userMap.getUsers()));
                    }
                    case Message.Type.message -> System.out.println("message: " + currentMessage.content);
                }
            });
            ws.onClose(ctx -> {
                userMap.removeUser(ctx.sessionId());
            });
        });
        app.start(7070);
    }
}