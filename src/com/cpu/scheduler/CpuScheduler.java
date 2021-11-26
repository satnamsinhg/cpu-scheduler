package com.cpu.scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CpuScheduler {

    private final List<CpuProcess> cpuProcessList;
    private final List<ProcessCallMarker> runningQueue = new ArrayList<>();
    private final int quantumTime;
    private int currentTime = 0;
    private int contextSwitching = 0;

    public CpuScheduler(List<CpuProcess> cpuProcessList, int quantumTime) {
        this.cpuProcessList = cpuProcessList;
        this.quantumTime = quantumTime;
    }

    public void startCpuProcess() {
        CpuProcess intialProcess = cpuProcessList.stream()
                .filter(process -> process.getArrivalTime() == 0)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("process at 0 arrival time not found."));

        runningQueue.add(new ProcessCallMarker(intialProcess.getProcessId()));
        System.out.println("*----*----*----*----*----*----*----*----*----*");
        int n = 1;
        for (int i = 0; i < n; i++) {
            executeProcess(i);
            n = runningQueue.size();
        }
        System.out.println("| " + runningQueue.stream()
                .map(marker -> marker.getStartTime() + " P" + marker.getProcessId() + " " + marker.getEndTime())
                .collect(Collectors.joining(" | ")) + " |");

        System.out.println("*----*----*----*----*----*----*----*----*----*");
        Integer totalWorkTime = cpuProcessList.stream().map(CpuProcess::getProcessId)
                .map(processId -> {
                    System.out.println("Process " + processId);
                    Integer creationTime = runningQueue.stream()
                            .filter(marker -> marker.getProcessId() == processId)
                            .reduce((one, two) -> one.getStartTime() < two.getStartTime() ? one : two)
                            .map(processCallMarker -> {
                                        System.out.println("Creation time : " + processCallMarker.getStartTime());
                                        return processCallMarker.getStartTime();
                                    }
                            )
                            .orElse(0);
                    Integer completionTime = runningQueue.stream()
                            .filter(marker -> marker.getProcessId() == processId)
                            .reduce((one, two) -> one.getEndTime() > two.getEndTime() ? one : two)
                            .map(processCallMarker -> {
                                System.out.println("Completion time : " + processCallMarker.getEndTime());
                                return processCallMarker.getEndTime();
                            }).orElse(0);
                    System.out.println("*----*----*----*----*");

                    return completionTime - creationTime;
                })
                .reduce(Integer::sum)
                .orElse(0);

        int throughPut = totalWorkTime / cpuProcessList.size();

        System.out.println("ThroughPut : " + throughPut);

        int averageWaitingTime = (totalWorkTime - cpuProcessList.stream()
                .map(CpuProcess::getBurstTime)
                .reduce(Integer::sum)
                .orElse(0)) / cpuProcessList.size();

        System.out.println("Average Waiting Time : " + averageWaitingTime);
        System.out.println("Context Switch : " + contextSwitching);

        int cpuUtilization = cpuProcessList.stream()
                .map(CpuProcess::getBurstTime)
                .reduce(Integer::sum).orElse(0) / currentTime;

        System.out.println("CPU Utilization : " + cpuUtilization);
        System.out.println("*----*----*----*----END----*----*----*----*----*");
    }

    private void executeProcess(int i) {
        ProcessCallMarker processCallMarker = this.runningQueue.get(i);

        runningQueue.addAll(cpuProcessList.stream()
                .filter(cpuProcess -> cpuProcess.getArrivalTime() > currentTime
                        && cpuProcess.getArrivalTime() <= currentTime + quantumTime)
                .map(CpuProcess::getProcessId)
                .map(ProcessCallMarker::new)
                .collect(Collectors.toList()));

        cpuProcessList.forEach(cpuProcess -> {
            if (cpuProcess.getProcessId() == processCallMarker.processId) {
                int remainingBurstTime = cpuProcess.getRemainingBurstTime();

                if (remainingBurstTime > quantumTime) {
                    cpuProcess.setRemainingBurstTime(remainingBurstTime - quantumTime);
                    processCallMarker.setStartTime(currentTime);
                    processCallMarker.setEndTime(currentTime + quantumTime);
                    currentTime = currentTime + quantumTime;
                } else {
                    processCallMarker.setStartTime(currentTime);
                    processCallMarker.setEndTime(currentTime + cpuProcess.getRemainingBurstTime());
                    currentTime = currentTime + cpuProcess.getRemainingBurstTime();
                    cpuProcess.setRemainingBurstTime(0);
                }

                if (cpuProcess.getRemainingBurstTime() > 0) {
                    contextSwitching += 1;
                    runningQueue.add(new ProcessCallMarker(cpuProcess.getProcessId()));
                }
                processCallMarker.setExecuted(true);
            }
        });
    }
}
