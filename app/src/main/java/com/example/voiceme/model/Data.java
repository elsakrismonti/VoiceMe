package com.example.voiceme.model;

import java.util.List;

public class Data {
    private Keys key = new Keys();
    private String data;
    private List<Integer> variations;

    public Data() {
    }

    public Data(Keys key, String data, List<Integer> variations) {
        this.key = key;
        this.data = data;
        this.variations = variations;
    }

    public Keys getKey() {
        return key;
    }

    public void setKey(Keys key) {
        this.key = key;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public List<Integer> getVariations() {
        return variations;
    }

    public void setVariations(List<Integer> variations) {
        this.variations = variations;
    }

    @Override
    public String toString() {
        return "Data{" +
                "key=" + key +
                ", data='" + data + '\'' +
                ", variations=" + variations +
                '}';
    }
}
