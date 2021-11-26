package com.cpu.scheduler;

import java.util.Objects;

public class CpuProcess {
    int processId;
    int arrivalTime;
    int burstTime;
    int waitingTime;
    int turnAroundTime;
    int remainingBurstTime;

    public CpuProcess(int process, int arrivalTime, int burstTime) {
        this.processId = process;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingBurstTime = burstTime;
    }

    public int getProcessId() {
        return processId;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getRemainingBurstTime() {
        return remainingBurstTime;
    }

    public void setRemainingBurstTime(int remainingBurstTime) {
        this.remainingBurstTime = remainingBurstTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CpuProcess that = (CpuProcess) o;
        return processId == that.processId && arrivalTime == that.arrivalTime && burstTime == that.burstTime;
    }

    @Override
    public int hashCode() {
        return Objects.hash(processId, arrivalTime, burstTime);
    }


    @Override
    public String toString() {
        return "     " + processId + "          " + arrivalTime + "          " + burstTime + "    ";
    }
}
