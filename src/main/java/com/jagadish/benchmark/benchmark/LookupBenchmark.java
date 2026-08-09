package com.jagadish.benchmark.benchmark;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.Values;

import com.jagadish.benchmark.database.CognoDBConnection;
import com.jagadish.benchmark.util.CSVWriter;
import com.jagadish.benchmark.util.Statistics;

public class LookupBenchmark {

    public void runBenchmark() {

        Driver driver = CognoDBConnection.getDriver();

        int warmupIterations = 20;
        int measuredIterations = 100;

        long[] times = new long[measuredIterations];

        try (Session session = driver.session()) {

            // Create index
            session.run(
                "CREATE INDEX person_id_index IF NOT EXISTS " +
                "FOR (p:Person) ON (p.id)"
            ).consume();

            // Warm-up
            for (int i = 0; i < warmupIterations; i++) {

                session.run(
                    "MATCH (p:Person {id:$id}) RETURN p",
                    Values.parameters("id", "5")
                ).consume();
            }

            // Measured queries
            for (int i = 0; i < measuredIterations; i++) {

                long start = System.nanoTime();

                session.run(
                    "MATCH (p:Person {id:$id}) RETURN p",
                    Values.parameters("id", "5")
                ).consume();

                long end = System.nanoTime();

                times[i] = end - start;
            }
        }

        double p50 = Statistics.p50(times);
        double p95 = Statistics.p95(times);

        System.out.println("------------------------------");
        System.out.println("LOOKUP BENCHMARK");
        System.out.println("------------------------------");
        System.out.println("Lookup Node       : 5");
        System.out.println("Index             : Person.id");
        System.out.println("Warm-up Iterations: " + warmupIterations);
        System.out.println("Measured Iterations: " + measuredIterations);
        System.out.println("P50 Latency       : " + p50 + " ms");
        System.out.println("P95 Latency       : " + p95 + " ms");

        CSVWriter.writeResult(
            "Lookup Benchmark",
            (long) p50,
            p95
        );
    }
}