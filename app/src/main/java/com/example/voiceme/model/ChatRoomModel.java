package com.example.voiceme.model;

import java.util.Date;
import java.util.List;

public class ChatRoomModel {
    private String id;
    private String user1;
    private String user2;
    private Date createAt = new Date();

    public ChatRoomModel(String id, String user1, String user2, Date createAt) {
        this.id = id;
        this.user1 = user1;
        this.user2 = user2;
        this.createAt = createAt;
    }

    public ChatRoomModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser1() {
        return user1;
    }

    public void setUser1(String user1) {
        this.user1 = user1;
    }

    public String getUser2() {
        return user2;
    }

    public void setUser2(String user2) {
        this.user2 = user2;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
}
