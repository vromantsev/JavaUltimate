package com.bobocode.mergesort;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadLocalRandom;

public class DemoApp {

    private static final int WORKLOAD = 1000000;

    public static void main(String[] args) {
        ForkJoinPool pool = ForkJoinPool.commonPool();
        List<Integer> source = generateRandomValues(WORKLOAD);

        System.out.println(pool.invoke(new MergeSortTask<>(source)));

        pool.shutdown();
    }

    private static List<Integer> generateRandomValues(final int workload) {
        var result = new ArrayList<Integer>(workload);
        for (int i = 0; i < workload; i++) {
            result.add(ThreadLocalRandom.current().nextInt(workload));
        }
        return result;
    }
}
