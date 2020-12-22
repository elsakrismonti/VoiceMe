package com.example.voiceme.model;

import java.util.List;

public class Data {
    private Keys key = new Keys();
    private RunningTime time = new RunningTime();
    private CompressionFactors compressionFactors = new CompressionFactors();
    private String data;
    private List<Integer> variations;

    public Data() {
    }

    public Data(Keys key, RunningTime time, CompressionFactors compressionFactors, String data, List<Integer> variations) {
        this.key = key;
        this.time = time;
        this.compressionFactors = compressionFactors;
        this.data = data;
        this.variations = variations;
    }

    public Keys getKey() {
        return key;
    }

    public void setKey(Keys key) {
        this.key = key;
    }

    public RunningTime getTime() {
        return time;
    }

    public void setTime(RunningTime time) {
        this.time = time;
    }

    public CompressionFactors getCompressionFactors() {
        return compressionFactors;
    }

    public void setCompressionFactors(CompressionFactors compressionFactors) {
        this.compressionFactors = compressionFactors;
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
}
