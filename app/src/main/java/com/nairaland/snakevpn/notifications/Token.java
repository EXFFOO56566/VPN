package com.nairaland.snakevpn.notifications;

import com.google.firebase.database.Exclude;

public class Token {

    @Exclude
    private String token;

    public Token(String token) {
        this.token = token;
    }

    public Token() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
