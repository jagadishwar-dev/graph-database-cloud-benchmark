package com.jagadish.benchmark.benchmark;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;

import com.jagadish.benchmark.database.CognoDBConnection;
import com.jagadish.benchmark.util.CSVWriter;
import com.jagadish.benchmark.util.Timer;

public class AggregationBenchmark {

    public void runBenchmark() {

        Driver driver = CognoDBConnection.getDriver();

        Timer timer = new Timer();

        timer.start();

        try (Session session = driver.session()) {

            session.run(
                "MATCH (p:Person) RETURN count(p)"
            ).list();

        }

        timer.stop();

        System.out.println("--------------------------------");
        System.out.println("AGGREGATION BENCHMARK");
        System.out.println("--------------------------------");
        System.out.println("Aggregation : COUNT(Person)");
        System.out.println("Execution Time : "
                + timer.getElapsedTimeInMilliseconds() + " ms");
        
        CSVWriter.writeResult(
        	    "Aggregation Benchmark",
        	    timer.getElapsedTimeInMilliseconds(),
        	    timer.getElapsedTimeInSeconds()
        	);
    }
}