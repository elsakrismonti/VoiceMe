package com.example.voiceme.model;

import java.util.Date;
import java.util.List;

public class ChatModel {

    private String id;
    private String senderId;
    private String data1;
    private List<Integer> variation1;
    private String data2;
    private List<Integer> variation2;
    private String data3;
    private List<Integer> variation3;
    private int prime;
    private String dataFinal;
    private Date createAt = new Date();

    public ChatModel(String senderId, String dataFinal, Date createAt) {
        this.senderId = senderId;
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

    public List<Integer> getVariation1() {
        return variation1;
    }

    public void setVariation1(List<Integer> variation1) {
        this.variation1 = variation1;
    }

    public String getData2() {
        return data2;
    }

    public void setData2(String data2) {
        this.data2 = data2;
    }

    public List<Integer> getVariation2() {
        return variation2;
    }

    public void setVariation2(List<Integer> variation2) {
        this.variation2 = variation2;
    }

    public String getData3() {
        return data3;
    }

    public void setData3(String data3) {
        this.data3 = data3;
    }

    public List<Integer> getVariation3() {
        return variation3;
    }

    public void setVariation3(List<Integer> variation3) {
        this.variation3 = variation3;
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