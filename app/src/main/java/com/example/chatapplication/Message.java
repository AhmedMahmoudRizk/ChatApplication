package com.example.chatapplication;

public class Message {
    private String text ;
    private MemberData memberData;
    private boolean belongsToCurrentUser;
    private String date;
    private boolean isTyping = false;

    public Message(String text, String date, MemberData data, boolean belongsToCurrentUser, boolean isTyping) {
        this.text = text;
        this.memberData = data;
        this.date = date;
        this.belongsToCurrentUser = belongsToCurrentUser;
        this.isTyping = isTyping;
    }

    public String getText() {
        return text;
    }

    public MemberData getMemberData() {
        return memberData;
    }

    public boolean isBelongsToCurrentUser() {
        return belongsToCurrentUser;
    }

    public boolean isTyping() {
        return isTyping;
    }

    public String getDate() {
        return date;
    }
}
