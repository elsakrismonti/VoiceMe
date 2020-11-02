package com.example.voiceme.model;

import java.util.Date;

public class ChatFinal {
    String chatFinal;
    private String senderId;

    public ChatFinal() {
    }

    public ChatFinal(String chatFinal, String senderId) {
        this.chatFinal = chatFinal;
        this.senderId = senderId;
    }

    public String getChatFinal() {
        return chatFinal;
    }

    public void setChatFinal(String chatFinal) {
        this.chatFinal = chatFinal;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
}
