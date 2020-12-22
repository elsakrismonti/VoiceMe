package com.example.voiceme.model;

public class Keys {
    private int p;
    private int e;
    private int d;

    public Keys() {
    }

    public Keys(int p, int e, int d) {
        this.p = p;
        this.e = e;
        this.d = d;
    }

    public int getP() {
        return p;
    }

    public void setP(int p) {
        this.p = p;
    }

    public int getE() {
        return e;
    }

    public void setE(int e) {
        this.e = e;
    }

    public int getD() {
        return d;
    }

    public void setD(int d) {
        this.d = d;
    }

    @Override
    public String toString() {
        return "Keys{" +
                "p=" + p +
                ", e=" + e +
                ", d=" + d +
                '}';
    }
}
