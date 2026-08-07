package com.jagadish.benchmark.benchmark;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;

import com.jagadish.benchmark.database.CognoDBConnection;
import com.jagadish.benchmark.util.CSVWriter;
import com.jagadish.benchmark.util.Timer;

public class TraversalBenchmark {

    public void runBenchmark() {

        Driver driver = CognoDBConnection.getDriver();

        Timer timer = new Timer();

        timer.start();

        try (Session session = driver.session()) {

            session.run(
                "MATCH (p:Person {id:$id})-[:CONNECTED_TO*1..3]->(friend) RETURN friend",
                org.neo4j.driver.Values.parameters("id", "1")
            ).list();

        }

        timer.stop();

        System.out.println("--------------------------------");
        System.out.println("TRAVERSAL BENCHMARK");
        System.out.println("--------------------------------");
        System.out.println("Traversal Start Node : 1");
        System.out.println("Traversal Depth      : 3");
        System.out.println("Execution Time       : "
                + timer.getElapsedTimeInMilliseconds() + " ms");
        
        CSVWriter.writeResult(
        	    "Traversal Benchmark",
        	    timer.getElapsedTimeInMilliseconds(),
        	    timer.getElapsedTimeInSeconds()
        	);
    }
}