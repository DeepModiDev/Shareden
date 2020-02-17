package com.deepmodi.shareden.model;

public class ChatRequest {
    private String senderNumber;
    private String senderName;
    private String senderLevel;
    private String senderImage;
    private String receiverNumber;
    private String receiverName;
    private String receiverLevel;
    private String receiverImage;
    private String senderRequestStatus;

    public ChatRequest() {
    }

    public ChatRequest(String senderNumber, String senderName, String senderLevel, String senderImage, String receiverNumber, String receiverName, String receiverLevel, String receiverImage, String senderRequestStatus) {
        this.senderNumber = senderNumber;
        this.senderName = senderName;
        this.senderLevel = senderLevel;
        this.senderImage = senderImage;
        this.receiverNumber = receiverNumber;
        this.receiverName = receiverName;
        this.receiverLevel = receiverLevel;
        this.receiverImage = receiverImage;
        this.senderRequestStatus = senderRequestStatus;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderLevel() {
        return senderLevel;
    }

    public void setSenderLevel(String senderLevel) {
        this.senderLevel = senderLevel;
    }

    public String getSenderImage() {
        return senderImage;
    }

    public void setSenderImage(String senderImage) {
        this.senderImage = senderImage;
    }

    public String getReceiverImage() {
        return receiverImage;
    }

    public void setReceiverImage(String receiverImage) {
        this.receiverImage = receiverImage;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverLevel() {
        return receiverLevel;
    }

    public void setReceiverLevel(String receiverLevel) {
        this.receiverLevel = receiverLevel;
    }

    public String getSenderNumber() {
        return senderNumber;
    }

    public void setSenderNumber(String senderNumber) {
        this.senderNumber = senderNumber;
    }

    public String getReceiverNumber() {
        return receiverNumber;
    }

    public void setReceiverNumber(String receiverNumber) {
        this.receiverNumber = receiverNumber;
    }

    public String getSenderRequestStatus() {
        return senderRequestStatus;
    }

    public void setSenderRequestStatus(String senderRequestStatus) {
        this.senderRequestStatus = senderRequestStatus;
    }
}
