package com.chatroom;

import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.Map;

public class Message {
    public enum Type {
        username, message
    }
    public Type type;
    public String content;

    public Message() {}

    @JsonSetter("HEADERS")
    public void setHeaders(Map<String, String> headers) {

        if (headers != null && headers.get("HX-Trigger") != null) {
            this.type = Type.valueOf(headers.get("HX-Trigger").toLowerCase());
        }
    }

    public Type getType() {
        return this.type;
    }

    public String getContent() {
        return this.content;
    }
}
