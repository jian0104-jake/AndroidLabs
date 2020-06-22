package com.example.androidlabs;

public class Message {
    private String message;
    private boolean isSend;
    protected long id;

    public Message(){

    }

    public Message(String message, boolean isSend, long id) {
        this.message = message;
        this.isSend = isSend;
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSend() {
        return isSend;
    }

    public void setSend(boolean send) {
        isSend = send;
    }
}
