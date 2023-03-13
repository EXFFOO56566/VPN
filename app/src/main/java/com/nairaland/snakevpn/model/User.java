package com.nairaland.snakevpn.model;

public class User {
    private String userid;
    private String status;
    private String type;
    private long lastseen;

    public User(String userid, String status, String type, long lastseen) {
        this.userid = userid;
        this.status = status;
        this.type = type;
        this.lastseen = lastseen;
    }

    public User() {
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getLastseen() {
        return lastseen;
    }

    public void setLastseen(long lastseen) {
        this.lastseen = lastseen;
    }
}
