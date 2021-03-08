package com.example.votingapplication.model;

public class Vote {

    private String uid;
    private String userUid;
    private String questionUid;
    private String optionUid;

    public Vote(String uid, String userUid, String questionUid, String optionUid) {
        this.uid = uid;
        this.userUid = userUid;
        this.questionUid = questionUid;
        this.optionUid = optionUid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getQuestionUid() {
        return questionUid;
    }

    public void setQuestionUid(String questionUid) {
        this.questionUid = questionUid;
    }

    public String getOptionUid() {
        return optionUid;
    }

    public void setOptionUid(String optionUid) {
        this.optionUid = optionUid;
    }
}
