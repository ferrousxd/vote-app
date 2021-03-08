package com.example.votingapplication.model;

import java.util.List;

public class Question {

    private String uid;
    private String title;
    private List<Option> options;

    public Question(String uid, String title, List<Option> options) {
        this.uid = uid;
        this.title = title;
        this.options = options;
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

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }
}
