package com.example.cryptochat.pojo;

import java.io.Serializable;
import java.util.Date;

public class ChatItem implements Serializable {
    private Contact contact;
    private String password, message;
    private Date time;
    private int numberUnreadMessages;

    public ChatItem(Contact contact, String password, String message, Date time, int numberUnreadMessages) {
        this.contact = contact;
        this.password = password;
        this.message = message;
        this.time = time;
        this.numberUnreadMessages = numberUnreadMessages;
    }

    public String getContactName() {
        return contact.getName();
    }

    public void setContactName(String contactName) {
        this.contact.setName(contactName);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public int getNumberUnreadMessages() {
        return numberUnreadMessages;
    }

    public void setNumberUnreadMessages(int numberUnreadMessages) {
        this.numberUnreadMessages = numberUnreadMessages;
    }

    public String getContactNumber() {
        return contact.getNumber();
    }

    public void setContactNumber(String contactNumber) {
        this.contact.setNumber(contactNumber);
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    @Override
    public String toString() {
        return "ChatItem{" +
                "contact=" + contact +
                ", password='" + password + '\'' +
                ", message='" + message + '\'' +
                ", time=" + time +
                ", numberUnreadMessages=" + numberUnreadMessages +
                '}';
    }
}

