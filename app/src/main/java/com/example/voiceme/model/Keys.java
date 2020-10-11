package com.example.voiceme.model;

public class Keys {
    private int p;
    private int d;

    public Keys() {
    }

    public Keys(int p, int d) {
        this.p = p;
        this.d = d;
    }

    public int getP() {
        return p;
    }

    public void setP(int p) {
        this.p = p;
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
                ", d=" + d +
                '}';
    }
}
