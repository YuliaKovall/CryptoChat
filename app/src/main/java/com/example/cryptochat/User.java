package com.example.cryptochat;

public class User {
    private String userName;
    private String message;
    private String time;
    private int count;

    public User(String userName, String message, String time, int count) {
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}

