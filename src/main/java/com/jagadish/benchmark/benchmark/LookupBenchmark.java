package com.jagadish.benchmark.benchmark;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;

import com.jagadish.benchmark.database.CognoDBConnection;
import com.jagadish.benchmark.util.CSVWriter;
import com.jagadish.benchmark.util.Timer;

public class LookupBenchmark {

    public void runBenchmark() {

        Driver driver = CognoDBConnection.getDriver();

        Timer timer = new Timer();

        timer.start();

        try (Session session = driver.session()) {

            session.run(
                "MATCH (p:Person {id:$id}) RETURN p",
                org.neo4j.driver.Values.parameters("id", "5")
            ).list();

        }

        timer.stop();

        System.out.println("------------------------------");
        System.out.println("LOOKUP BENCHMARK");
        System.out.println("------------------------------");
        System.out.println("Lookup Node : 5");
        System.out.println("Execution Time : "
                + timer.getElapsedTimeInMilliseconds() + " ms");
        
        CSVWriter.writeResult(
        	    "Lookup Benchmark",
        	    timer.getElapsedTimeInMilliseconds(),
        	    timer.getElapsedTimeInSeconds()
        	);
        
    }
}