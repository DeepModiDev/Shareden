package com.deepmodi.shareden.model;

public class UserChatConModel {
    private String senderName;
    private String senderNumber;
    private String receiverName;
    private String receiverNumber;
    private String user1messsage;
    private String user2message;

    public UserChatConModel() {
    }

    public UserChatConModel(String senderNumber, String receiverNumber, String user1messsage, String user2message) {
        this.senderNumber = senderNumber;
        this.receiverNumber = receiverNumber;
        this.user1messsage = user1messsage;
        this.user2message = user2message;
    }

    public UserChatConModel(String senderName, String senderNumber, String receiverName, String receiverNumber, String user1messsage, String user2message) {
        this.senderName = senderName;
        this.senderNumber = senderNumber;
        this.receiverName = receiverName;
        this.receiverNumber = receiverNumber;
        this.user1messsage = user1messsage;
        this.user2message = user2message;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderNumber() {
        return senderNumber;
    }

    public void setSenderNumber(String senderNumber) {
        this.senderNumber = senderNumber;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverNumber() {
        return receiverNumber;
    }

    public void setReceiverNumber(String receiverNumber) {
        this.receiverNumber = receiverNumber;
    }

    public String getUser1messsage() {
        return user1messsage;
    }

    public void setUser1messsage(String user1messsage) {
        this.user1messsage = user1messsage;
    }

    public String getUser2message() {
        return user2message;
    }

    public void setUser2message(String user2message) {
        this.user2message = user2message;
    }
}
