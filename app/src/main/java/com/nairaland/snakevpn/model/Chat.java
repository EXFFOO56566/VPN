package com.nairaland.snakevpn.model;

import com.google.firebase.database.Exclude;

public class Chat {

    private String sender;
    private String receiver;
    private String message;
    private String chatid;
    private boolean isseen;
    private long time;

    public Chat(String sender, String receiver, String message, String chatid, boolean isseen, long time) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.chatid = chatid;
        this.isseen = isseen;
        this.time = time;
    }

    public Chat() {
    }

    public String getSender() {
        return sender;
    }

    @Exclude
    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    @Exclude
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    @Exclude
    public void setMessage(String message) {
        this.message = message;
    }

    public String getChatid() {
        return chatid;
    }

    @Exclude
    public void setChatid(String chatid) {
        this.chatid = chatid;
    }

    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
