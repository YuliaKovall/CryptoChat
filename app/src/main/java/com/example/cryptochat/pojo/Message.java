package com.example.cryptochat.pojo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {

    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    private String message;
    private boolean isSentByCurrentUser;
    private Date time;

    public Message(String message, boolean isSentByCurrentUser, Date time) {
        this.message = message;
        this.isSentByCurrentUser = isSentByCurrentUser;
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSentByCurrentUser() {
        return isSentByCurrentUser;
    }

    public Date getTime() {
        return time;
    }

    public String getFormattedTime() {
        return timeFormat.format(time.getTime());
    }
}
