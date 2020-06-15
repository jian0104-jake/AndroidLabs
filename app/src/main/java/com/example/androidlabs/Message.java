package com.example.androidlabs;

public class Message {
    private String messgage;
    private boolean isSend;

    public Message(){

    }

    public Message(String message, boolean isSend){
    this.messgage = message;
    this.isSend = isSend;
    }
    public String getMessgage() {
        return messgage;
    }

    public void setMessgage(String messgage) {
        this.messgage = messgage;
    }

    public boolean isSend() {
        return isSend;
    }

    public void setSend(boolean send) {
        isSend = send;
    }
}
