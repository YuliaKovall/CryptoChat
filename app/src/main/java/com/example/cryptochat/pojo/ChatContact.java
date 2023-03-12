package com.example.cryptochat.pojo;

import java.io.Serializable;
import java.util.List;

public class ChatContact implements Serializable {
    private Contact contact;
    private List<Message> listMessage;

    public ChatContact(String contactId, String contactName, String contactNumber, List<Message> listMessage) {
        this.contact = new Contact(contactId, contactName, contactNumber);
        this.listMessage = listMessage;
    }

    public ChatContact(Contact contact, List<Message> listMessege) {
        this.contact = contact;
        this.listMessage = listMessege;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public List<Message> getListMessage() {
        return listMessage;
    }

    public void setListMessage(List<Message> listMessege) {
        this.listMessage = listMessege;
    }
}
