package com.jagadish.benchmark.util;

import java.util.Arrays;

public class Statistics {

    public static double percentile(long[] values, double percentile) {

        long[] sorted = values.clone();

        Arrays.sort(sorted);

        int index = (int) Math.ceil(
                (percentile / 100.0) * sorted.length
        ) - 1;

        if (index < 0) {
            index = 0;
        }

        if (index >= sorted.length) {
            index = sorted.length - 1;
        }

        return sorted[index] / 1_000_000.0;
    }

    public static double p50(long[] values) {
        return percentile(values, 50);
    }

    public static double p95(long[] values) {
        return percentile(values, 95);
    }
}