package com.chatroom;

import j2html.tags.ContainerTag;
import j2html.tags.specialized.DivTag;

import java.util.ArrayList;
import java.util.stream.Stream;

import static j2html.TagCreator.*;

public class HTMLFactory {

    public HTMLFactory() {
    }

    public String getMainHTML() {
        return html(
                head(
                        script().withSrc("https://unpkg.com/htmx.org@2.0.4").attr("integrity", "sha384-HGfztofotfshcF7+8n44JQL2oJmowVChPTg48S+jvZoztPfvwD79OC/LTtG6dMp+").attr("crossorigin", "anonymous"),
                        script().withSrc("https://unpkg.com/htmx.org@1.9.12/dist/ext/ws.js"),
                        script().withSrc("https://cdn.tailwindcss.com")
                ),
                body(
                        div(
                                div(
                                        h1("Java chat room").withClass("text-3xl font-bold underline text-center m-4"),
                                        div(
                                                // sign in button on initial load
                                                div(
                                                        form(
                                                                input()
                                                                        .withClass("flex-1 p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500")
                                                                        .withName("content")
                                                                        .withPlaceholder("Choose your username")
                                                                        .withType("text")
                                                                        .attr("autocomplete", "off"),
                                                                button("Connect")
                                                                        .withType("submit")
                                                                        .withClass("bg-blue-500 text-white px-6 py-2 rounded-lg hover:bg-blue-600 transition-colors")
                                                        ).withId("username").attr("ws-send").withClass("flex gap-2")
                                                )
                                        ).withId("main-section").withClass("w-[70%]")
                                ).withClass("flex flex-col gap-4 justify-center items-center")
                        )
                                .attr("hx-ext", "ws")
                                .attr("ws-connect", "ws://localhost:7070/chat-socket")
                                .attr("ws-disconnect")
                )
        ).render();
    }

    public String getChatViewHTML(String username, ArrayList<DBMessage> messages) {
        var messagesAsHTML = messages.stream().map(msg -> this.getMessageHTML(msg.getContent(), msg.getUsername(), msg.getUsername().equals(username))).toArray(ContainerTag[]::new);
        return div(
                // logged in message
                h2(String.format("Logged in as: %s", username)),

                // chat window
                div(
                        messagesAsHTML
                )
                        .withClass("h-96 overflow-y-auto border rounded-lg bg-gray-50 p-4 my-4 space-y-4 w-[100%]")
                        .withId("chat-window"),
                // send message button
                div(
                        form(
                                input()
                                        .withClass("flex-1 p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500")
                                        .withName("content")
                                        .withType("text")
                                        .withId("send-message-input")
                                        .attr("autocomplete", "off"),
                                button("Send")
                                        .withType("submit")
                                        .withClass("bg-blue-500 text-white px-6 py-2 rounded-lg hover:bg-blue-600 transition-colors")
                        )
                                .withId("message")
                                .attr("ws-send")
                                .withClass("flex gap-4")
                )
        ).attr("hx-swap-oob", "innerHTML:#main-section").render();
    }

    public DivTag getMessageHTML(String message, String username, boolean owned) {
        if (owned) {
            return div(
                    p("You").withClass("text-sm text-gray-500 text-right mr-2"),
                    div(
                            div(
                                    p(message)
                            ).withClass("bg-blue-500 text-white rounded-lg rounded-tr-none p-3 shadow-sm max-w-[80%]")
                    ).withClass("flex justify-end"),
                    script("document.getElementById('send-message-input').value = ''; document.getElementById('chat-window').scrollTop = document.getElementById('chat-window').scrollHeight;").attr("hx-swap-oob", "true")
            ).withClass("space-y-1").attr("hx-swap-oob", "beforeend:#chat-window");
        } else {
            return div(
                    p(username).withClass("text-sm text-gray-500 ml-2"),
                    div(
                            div(
                                    p(message)
                            ).withClass("bg-white rounded-lg rounded-tl-none p-3 shadow-sm max-w-[80%]")
                    ).withClass("flex justify-start"),
                    script("document.getElementById('send-message-input').value = ''; document.getElementById('chat-window').scrollTop = document.getElementById('chat-window').scrollHeight;").attr("hx-swap-oob", "true")
            ).withClass("space-y-1").attr("hx-swap-oob", "beforeend:#chat-window scroll:bottom");
        }
    }
}
