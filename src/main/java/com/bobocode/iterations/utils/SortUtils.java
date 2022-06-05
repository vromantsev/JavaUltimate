package com.bobocode.iterations.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class SortUtils {

    private SortUtils() {}

    public static List<Integer> generateRandomNums(final int range) {
        var list = new ArrayList<Integer>();
        for (int i = 0; i < range; i++) {
            list.add(ThreadLocalRandom.current().nextInt(range));
        }
        return list;
    }
}

