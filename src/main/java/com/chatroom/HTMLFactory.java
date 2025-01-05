package com.chatroom;

import java.util.Collection;

public class HTMLFactory {

    public HTMLFactory() {}

    public String getUsersHTML(Collection<String> users) {
        StringBuilder HTMLBuilder = new StringBuilder();
        HTMLBuilder.append(
                """
                <div hx-swap-oob="beforeend:#connect_status">
                  <h6>Connected users:</h6>
                """);
        users.forEach((user) -> {
            HTMLBuilder.append("<p>").append(user).append("</p>");
        });
        HTMLBuilder.append("</div>");

        return HTMLBuilder.toString();
    }
}
