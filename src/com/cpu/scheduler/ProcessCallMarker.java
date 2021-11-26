package com.cpu.scheduler;

public class ProcessCallMarker {
    int processId;
    int startTime;
    int endTime;
    boolean isExecuted;

    public ProcessCallMarker(int processId) {
        this.processId = processId;
    }

    public void setExecuted(boolean executed) {
        isExecuted = executed;
    }

    public int getProcessId() {
        return processId;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }
}
