package com.jagadish.benchmark;

import java.io.File;

import com.jagadish.benchmark.benchmark.AggregationBenchmark;
import com.jagadish.benchmark.benchmark.InsertBenchmark;
import com.jagadish.benchmark.benchmark.LookupBenchmark;
import com.jagadish.benchmark.benchmark.MixedWorkloadBenchmark;
import com.jagadish.benchmark.benchmark.TraversalBenchmark;
import com.jagadish.benchmark.database.CognoDBConnection;
import com.jagadish.benchmark.loader.DataLoader;

public class Main {

    public static void main(String[] args) {
    	
    	File file = new File("results/benchmark.csv");
    	if (file.exists()) {
    	    file.delete();
    	}

        // Connect to CognoDB
        CognoDBConnection.getDriver();

        System.out.println("Benchmark Project Started...");
        
        // Load CSV file
        InsertBenchmark insertBenchmark = new InsertBenchmark();
        insertBenchmark.runBenchmark();

        LookupBenchmark lookupBenchmark = new LookupBenchmark();
        lookupBenchmark.runBenchmark();
        
        TraversalBenchmark traversalBenchmark = new TraversalBenchmark();
        traversalBenchmark.runBenchmark();
        
        AggregationBenchmark aggregationBenchmark = new AggregationBenchmark();
        aggregationBenchmark.runBenchmark();
        
        MixedWorkloadBenchmark mixedBenchmark = new MixedWorkloadBenchmark();
        mixedBenchmark.runBenchmark();

        // Close connection
        CognoDBConnection.closeConnection();
    }
}