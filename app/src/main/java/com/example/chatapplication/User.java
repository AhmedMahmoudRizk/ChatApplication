package com.example.chatapplication;

import android.widget.ImageView;

public class User {
    ImageView avatar;
    String name;

    public User(String name) {
        this.avatar = avatar;
        this.name = name;
    }

    public ImageView getAvatar() {
        return avatar;
    }

    public String getNamee() {
        return name;
    }

    public void setAvatar(ImageView avatar) {
        this.avatar = avatar;
    }

    public void setNamee(String name) {
        this.name = name;
    }
}
