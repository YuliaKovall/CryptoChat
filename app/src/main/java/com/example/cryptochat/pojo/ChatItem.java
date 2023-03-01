package com.example.cryptochat.pojo;

import java.util.Date;

public class ChatItem {
    private Contact contact;
    private String message;
    private Date time;
    private int numberUnreadMessages;

    public ChatItem(String contactId, String contactName, String contactNumber, String message, Date time, int numberUnreadMessages) {
        this.contact = new Contact(contactId, contactName, contactNumber);
        this.message = message;
        this.time = time;
        this.numberUnreadMessages = numberUnreadMessages;
    }

    public ChatItem(Contact contact, String message, Date time, int numberUnreadMessages) {
        this.contact = contact;
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
}

