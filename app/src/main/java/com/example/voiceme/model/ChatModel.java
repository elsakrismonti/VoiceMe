package com.example.voiceme.model;

import com.google.firebase.Timestamp;

import java.util.Date;

public class ChatModel {

    private String id;
    private String senderId;
    private String data1;
    private String data2;
    private String data3;
    private int prime;
    private String dataFinal;
    private Date createAt = new Date();

    public ChatModel(String id, String senderId, String data1, String data2, String data3, int prime, String dataFinal, Date createAt) {
        this.id = id;
        this.senderId = senderId;
        this.data1 = data1;
        this.data2 = data2;
        this.data3 = data3;
        this.prime = prime;
        this.dataFinal = dataFinal;
        this.createAt = createAt;
    }

    public ChatModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getData1() {
        return data1;
    }

    public void setData1(String data1) {
        this.data1 = data1;
    }

    public String getData2() {
        return data2;
    }

    public void setData2(String data2) {
        this.data2 = data2;
    }

    public String getData3() {
        return data3;
    }

    public void setData3(String data3) {
        this.data3 = data3;
    }

    public int getPrime() {
        return prime;
    }

    public void setPrime(int prime) {
        this.prime = prime;
    }

    public String getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(String dataFinal) {
        this.dataFinal = dataFinal;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
}