package com.chatroom;

import java.util.Collection;

import static j2html.TagCreator.div;
import static j2html.TagCreator.p;

public class HTMLFactory {

    public HTMLFactory() {
    }

    public String getMessageHTML(String message, String username, boolean owned) {
        if (owned) {
            return div(
                    p("You").withClass("text-sm text-gray-500 text-right mr-2"),
                    div(
                            div(
                                    p(message)
                            ).withClass("bg-blue-500 text-white rounded-lg rounded-tr-none p-3 shadow-sm max-w-[80%]")
                    ).withClass("flex justify-end")
            ).withClass("space-y-1").attr("hx-swap-oob", "beforeend:#chat-window").render();
        } else {
            return div(
                    p(username).withClass("text-sm text-gray-500 ml-2"),
                    div(
                            div(
                                    p(message)
                            ).withClass("bg-white rounded-lg rounded-tl-none p-3 shadow-sm max-w-[80%]")
                    ).withClass("flex justify-start")
            ).withClass("space-y-1").attr("hx-swap-oob", "beforeend:#chat-window").render();
        }
    }
}
