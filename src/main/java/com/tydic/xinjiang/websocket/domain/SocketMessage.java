package com.tydic.xinjiang.websocket.domain;

import java.util.Map;

public class SocketMessage {
    private Map data;
    private String message;
    private String messageType;

    public Map getData() {
        return data;
    }

    public void setData(Map data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
}
