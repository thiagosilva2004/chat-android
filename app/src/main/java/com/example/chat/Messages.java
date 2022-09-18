package com.example.chat;

public class Messages {
    private String message;
    private long time;
    private String fromID;
    private String toID;

    public Messages(String message, long time, String fromID, String toID) {
        this.message = message;
        this.time = time;
        this.fromID = fromID;
        this.toID = toID;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getFromID() {
        return fromID;
    }

    public void setFromID(String fromID) {
        this.fromID = fromID;
    }

    public String getToID() {
        return toID;
    }

    public void setToID(String toID) {
        this.toID = toID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
