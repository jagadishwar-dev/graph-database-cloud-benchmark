package com.jagadish.benchmark.benchmark;

import com.jagadish.benchmark.loader.DataLoader;
import com.jagadish.benchmark.util.CSVWriter;
import com.jagadish.benchmark.util.Timer;

public class InsertBenchmark {

    public void runBenchmark() {

        Timer timer = new Timer();

        timer.start();

        DataLoader loader = new DataLoader();
        loader.loadCSV("src/main/resources/dataset/graph.csv");

        timer.stop();

        int nodes = 2000;
        int relationships = 4000;

        System.out.println("--------------------------------");
        System.out.println("INSERT BENCHMARK");
        System.out.println("--------------------------------");

        System.out.println("Nodes Inserted        : " + nodes);
        System.out.println("Relationships Inserted: " + relationships);

        System.out.println("Execution Time        : "
                + timer.getElapsedTimeInMilliseconds() + " ms");

        double seconds = timer.getElapsedTimeInSeconds();

        System.out.println("Execution Time        : "
                + seconds + " seconds");
        
        CSVWriter.writeResult(
        	    "Insert Benchmark",
        	    timer.getElapsedTimeInMilliseconds(),
        	    seconds
        );

        System.out.println("Nodes/sec             : "
                + (nodes / seconds));

        System.out.println("Relationships/sec     : "
                + (relationships / seconds));
    }
}