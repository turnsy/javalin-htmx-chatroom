package com.chatroom;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        // global maps used
        UserMap userMap = new UserMap();
        ObjectMapper messageMapper = new ObjectMapper();
        HTMLFactory HtmlFactory = new HTMLFactory();
        DBConn dbConn = new DBConn();

        var app = Javalin.create(config -> {
        });
        app.get("/", ctx -> {
            ctx.html(HtmlFactory.getMainHTML());
        });

        app.ws("/chat-socket", ws -> {
            ws.onMessage(ctx -> {
                Message currentMessage = messageMapper.readValue(ctx.message(), Message.class);
                switch (currentMessage.getType()) {

                    // send back chat box HTML on 'connection'
                    case Message.Type.username -> {
                        userMap.addUser(ctx.sessionId(), currentMessage.content, ctx);
                        ctx.send(HtmlFactory.getChatViewHTML(currentMessage.content, dbConn.getMessages(10)));
                    }

                    case Message.Type.message -> {
                        String sourceUsername = userMap.get(ctx.sessionId()).username();

                        // First add to database, for persistence
                        dbConn.addMessage(sourceUsername, currentMessage.getContent());

                        // broadcast to all users, with different HTML depending on recipient
                        for (String session : userMap.keySet()) {
                            UserSession userSession = userMap.get(session);
                            userSession.context().send(HtmlFactory.getMessageHTML(currentMessage.content, sourceUsername, session.equals(ctx.sessionId())).render());
                        }
                    }
                }
            });
            ws.onClose(ctx -> {
                userMap.removeUser(ctx.sessionId());
            });
        });
        app.start(7070);

        //dbConn.close();
    }
}