package com.example.chatapplication;

public class Message {
    private String text;
    private MemberData memberData;
    private boolean belongsToCurrentUser;
    private String date;

    public Message(String text, String date, MemberData data, boolean belongsToCurrentUser) {
        this.text = text;
        this.memberData = data;
        this.date = date;
        this.belongsToCurrentUser = belongsToCurrentUser;
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

    public String getDate() {
        return date;
    }
}
