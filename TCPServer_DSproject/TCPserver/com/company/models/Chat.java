package com.company.models;
import java.util.Date;

public class Chat {
    private int senderID;
    private int receiverID;
    private Date sendTime;
    private String message;
    private String path;

    public Chat(int senderID, int receiverID, Date timeSent, String message, String path) {
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.sendTime = timeSent;
        this.message = message;
        this.path = path;
    }
    public Chat(){
        this.senderID=1;
        this.receiverID=1;
        this.sendTime =null;
        this.message="";
        this.path="";
    }

    public int getSenderID() {
        return senderID;
    }

    public void setSenderID(int senderID) {
        this.senderID = senderID;
    }

    public int getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(int receiverID) {
        this.receiverID = receiverID;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
