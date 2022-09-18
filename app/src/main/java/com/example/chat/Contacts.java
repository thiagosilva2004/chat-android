package com.example.chat;

public class Contacts {
    private String uuid;
    private String username;
    private String lastMessage;
    private long time;
    private String profileURL;

    public Contacts(String uuid, String username, String lastMessage, long time, String profileURL) {
        this.uuid = uuid;
        this.username = username;
        this.lastMessage = lastMessage;
        this.time = time;
        this.profileURL = profileURL;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getProfileURL() {
        return profileURL;
    }

    public void setProfileURL(String profileURL) {
        this.profileURL = profileURL;
    }
}
