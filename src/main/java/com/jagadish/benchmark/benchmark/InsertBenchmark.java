package com.jagadish.benchmark.benchmark;

import com.jagadish.benchmark.loader.DataLoader;
import com.jagadish.benchmark.loader.DataLoader.LoadResult;
import com.jagadish.benchmark.util.CSVWriter;
import com.jagadish.benchmark.util.Timer;

public class InsertBenchmark {

    public void runBenchmark() {

        Timer timer = new Timer();

        timer.start();

        DataLoader loader = new DataLoader();

        LoadResult result = loader.loadCSV(
            "src/main/resources/dataset/graph.csv"
        );

        timer.stop();

        int nodes = result.getNodes();
        int relationships = result.getRelationships();

        double seconds =
            timer.getElapsedTimeInSeconds();

        double milliseconds =
            timer.getElapsedTimeInMilliseconds();

        double relationshipsPerSecond =
            relationships / seconds;

        double nodesPerSecond =
            nodes / seconds;

        System.out.println("--------------------------------");
        System.out.println("INSERT BENCHMARK");
        System.out.println("--------------------------------");

        System.out.println(
            "Nodes                 : " + nodes
        );

        System.out.println(
            "Relationships Inserted: " + relationships
        );

        System.out.println(
            "Execution Time        : "
            + milliseconds
            + " ms"
        );

        System.out.println(
            "Execution Time        : "
            + seconds
            + " seconds"
        );

        System.out.println(
            "Nodes/sec             : "
            + nodesPerSecond
        );

        System.out.println(
            "Relationships/sec     : "
            + relationshipsPerSecond
        );

        CSVWriter.writeIngestResult(
            nodes,
            relationships,
            milliseconds,
            nodesPerSecond,
            relationshipsPerSecond
        );
    }
}