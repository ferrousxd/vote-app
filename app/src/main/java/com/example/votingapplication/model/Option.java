package com.example.votingapplication.model;

public class Option {

    private String uid;
    private String title;

    public Option(String uid, String title) {
        this.uid = uid;
        this.title = title;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}