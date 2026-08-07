package com.jagadish.benchmark.benchmark;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.Values;

import com.jagadish.benchmark.database.CognoDBConnection;
import com.jagadish.benchmark.util.CSVWriter;
import com.jagadish.benchmark.util.Timer;

public class MixedWorkloadBenchmark {

    public void runBenchmark() {

        Driver driver = CognoDBConnection.getDriver();

        Timer timer = new Timer();

        timer.start();

        try (Session session = driver.session()) {

            // Lookup
            session.run(
                    "MATCH (p:Person {id:$id}) RETURN p",
                    Values.parameters("id", "5")
            ).list();

            // Traversal
            session.run(
                    "MATCH (p:Person {id:$id})-[:CONNECTED_TO*1..3]->(f) RETURN f",
                    Values.parameters("id", "1")
            ).list();

            // Aggregation
            session.run(
                    "MATCH (p:Person) RETURN count(p)"
            ).list();

        }

        timer.stop();

        System.out.println("--------------------------------");
        System.out.println("MIXED WORKLOAD BENCHMARK");
        System.out.println("--------------------------------");
        System.out.println("Operations Performed : Lookup + Traversal + Aggregation");
        System.out.println("Execution Time       : "
                + timer.getElapsedTimeInMilliseconds() + " ms");
        
        CSVWriter.writeResult(
        	    "Mixed Workload Benchmark",
        	    timer.getElapsedTimeInMilliseconds(),
        	    timer.getElapsedTimeInSeconds()
        	);
    }
}