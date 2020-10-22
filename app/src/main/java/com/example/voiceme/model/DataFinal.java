package com.example.voiceme.model;

public class DataFinal {
    String dataFinal;
    RunningTime time = new RunningTime();

    public DataFinal() {
    }

    public DataFinal(String dataFinal, RunningTime time) {
        this.dataFinal = dataFinal;
        this.time = time;
    }

    public String getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(String dataFinal) {
        this.dataFinal = dataFinal;
    }

    public RunningTime getTime() {
        return time;
    }

    public void setTime(RunningTime time) {
        this.time = time;
    }
}
