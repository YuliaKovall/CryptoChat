package com.example.cryptochat;

import java.util.Date;

public class User {
    private String userName;
    private String message;
    private Date time;
    private int count;

    public User(String userName, String message, Date time, int count) {
        this.userName = userName;
        this.message = message;
        this.time = time;
        this.count = count;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}

