package com.cpu.scheduler;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class AppRunner {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(new File(args[0]));
        sc.useDelimiter(",|\\r?\\n");
        List<CpuProcess> processList = new ArrayList<>();
        sc.nextLine();
        while (sc.hasNextLine()) {
            processList.add(new CpuProcess(sc.nextInt(), sc.nextInt(), sc.nextInt()));
        }
        sc.close();

        System.out.println("*----*----*----*----START----*----*----*----*----*");
        System.out.println("ProcessTime ArrivalTime BurstTime");
        processList.stream().forEach(System.out::println);

        CpuScheduler cpuScheduler = new CpuScheduler(processList, parseInt(args[1]));
        cpuScheduler.startCpuProcess();
    }
}
