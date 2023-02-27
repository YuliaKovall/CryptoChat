package com.example.cryptochat;

import java.text.SimpleDateFormat;

public class Message {

    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm");
    private String message;
    private boolean isSentByCurrentUser;
    private long timeStamp;

    public Message(String message, boolean isSentByCurrentUser, long timeStamp) {
        this.message = message;
        this.isSentByCurrentUser = isSentByCurrentUser;
        this.timeStamp = timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSentByCurrentUser() {
        return isSentByCurrentUser;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public String getFormattedTime() {
        return timeFormat.format(timeStamp);
    }
}
