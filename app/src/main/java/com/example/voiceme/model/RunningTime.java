package com.example.voiceme.model;

public class RunningTime {

    private double encryptionTime;
    private double decryptionTime;
    private double compressionTime;
    private double decompressionTime;

    public RunningTime() {}

    public RunningTime(double encryptionTime, double decryptionTime, double compressionTime, double decompressionTime) {
        this.encryptionTime = encryptionTime;
        this.decryptionTime = decryptionTime;
        this.compressionTime = compressionTime;
        this.decompressionTime = decompressionTime;
    }

    public double getEncryptionTime() {
        return encryptionTime;
    }

    public void setEncryptionTime(double encryptionTime) {
        this.encryptionTime = encryptionTime;
    }

    public double getDecryptionTime() {
        return decryptionTime;
    }

    public void setDecryptionTime(double decryptionTime) {
        this.decryptionTime = decryptionTime;
    }

    public double getCompressionTime() {
        return compressionTime;
    }

    public void setCompressionTime(double compressionTime) {
        this.compressionTime = compressionTime;
    }

    public double getDecompressionTime() {
        return decompressionTime;
    }

    public void setDecompressionTime(double decompressionTime) {
        this.decompressionTime = decompressionTime;
    }
}
