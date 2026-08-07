package com.jagadish.benchmark.util;

public class Timer {

    private long startTime;
    private long endTime;

    public void start() {
        startTime = System.nanoTime();
    }

    public void stop() {
        endTime = System.nanoTime();
    }

    public double getElapsedTimeInSeconds() {
        return (endTime - startTime) / 1_000_000_000.0;
    }

    public long getElapsedTimeInMilliseconds() {
        return (endTime - startTime) / 1_000_000;
    }
}