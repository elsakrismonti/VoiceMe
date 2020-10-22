package com.example.voiceme.model;

import java.util.Date;

public class ChatModel {

    private String id;
    private String senderId;
    private Data data1 = new Data();
    private Data data2 = new Data();
    private Data data3 = new Data();
    private DataFinal dataFinal = new DataFinal();
    private Date createAt = new Date();

    public ChatModel() {
    }

    public ChatModel(String id, String senderId, Data data1, Data data2, Data data3, DataFinal dataFinal, Date createAt) {
        this.id = id;
        this.senderId = senderId;
        this.data1 = data1;
        this.data2 = data2;
        this.data3 = data3;
        this.dataFinal = dataFinal;
        this.createAt = createAt;
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

    public Data getData1() {
        return data1;
    }

    public void setData1(Data data1) {
        this.data1 = data1;
    }

    public Data getData2() {
        return data2;
    }

    public void setData2(Data data2) {
        this.data2 = data2;
    }

    public Data getData3() {
        return data3;
    }

    public void setData3(Data data3) {
        this.data3 = data3;
    }

    public DataFinal getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(DataFinal dataFinal) {
        this.dataFinal = dataFinal;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    @Override
    public String toString() {
        return "ChatModel{" +
                "id='" + id + '\'' +
                ", senderId='" + senderId + '\'' +
                ", data1=" + data1 +
                ", data2=" + data2 +
                ", data3=" + data3 +
                ", dataFinal='" + dataFinal + '\'' +
                ", createAt=" + createAt +
                '}';
    }
}