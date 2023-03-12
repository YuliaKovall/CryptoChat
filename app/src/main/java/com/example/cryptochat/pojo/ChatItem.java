package com.example.cryptochat.pojo;

import java.io.Serializable;
import java.util.Date;

public class ChatItem implements Serializable {
    private Contact contact;
    private Message lastMessage;
    private int numberUnreadMessages;

    public ChatItem(String contactId, String contactName, String contactNumber, String message, boolean isSentByCurrentUser, Date time, int numberUnreadMessages) {
        this.contact = new Contact(contactId, contactName, contactNumber);
        this.lastMessage = new Message(message, isSentByCurrentUser, time);
        this.numberUnreadMessages = numberUnreadMessages;
    }

    public ChatItem(Contact contact, Message lastMessage, int numberUnreadMessages) {
        this.contact = contact;
        this.lastMessage = lastMessage;
        this.numberUnreadMessages = numberUnreadMessages;
    }

    public String getContactName() {
        return contact.getName();
    }

    public void setContactName(String contactName) {
        this.contact.setName(contactName);
    }


    public int getNumberUnreadMessages() {
        return numberUnreadMessages;
    }

    public void setNumberUnreadMessages(int numberUnreadMessages) {
        this.numberUnreadMessages = numberUnreadMessages;
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
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

