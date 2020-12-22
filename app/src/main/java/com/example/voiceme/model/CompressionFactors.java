package com.example.voiceme.model;

public class CompressionFactors {
    private double ratioOfCompression;
    private double CompressionRatio;
    private double spaceSavings;
    private double bitRate;

    public CompressionFactors() {
    }

    public CompressionFactors(double ratioOfCompression, double compressionRatio, double spaceSavings, double bitRate) {
        this.ratioOfCompression = ratioOfCompression;
        CompressionRatio = compressionRatio;
        this.spaceSavings = spaceSavings;
        this.bitRate = bitRate;
    }

    public double getRatioOfCompression() {
        return ratioOfCompression;
    }

    public void setRatioOfCompression(double ratioOfCompression) {
        this.ratioOfCompression = ratioOfCompression;
    }

    public double getCompressionRatio() {
        return CompressionRatio;
    }

    public void setCompressionRatio(double compressionRatio) {
        CompressionRatio = compressionRatio;
    }

    public double getSpaceSavings() {
        return spaceSavings;
    }

    public void setSpaceSavings(double spaceSavings) {
        this.spaceSavings = spaceSavings;
    }

    public double getBitRate() {
        return bitRate;
    }

    public void setBitRate(double bitRate) {
        this.bitRate = bitRate;
    }

    @Override
    public String toString() {
        return "CompressionFactors{" +
                "ratioOfCompression=" + ratioOfCompression +
                ", CompressionRatio=" + CompressionRatio +
                ", spaceSavings=" + spaceSavings +
                ", bitRate=" + bitRate +
                '}';
    }
}
