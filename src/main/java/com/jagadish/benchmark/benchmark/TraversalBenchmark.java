package com.jagadish.benchmark.benchmark;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.Values;

import com.jagadish.benchmark.database.CognoDBConnection;
import com.jagadish.benchmark.util.CSVWriter;
import com.jagadish.benchmark.util.Statistics;

public class TraversalBenchmark {

    public void runBenchmark() {

        Driver driver = CognoDBConnection.getDriver();

        int warmupIterations = 20;
        int measuredIterations = 100;

        long[] oneHopTimes = new long[measuredIterations];
        long[] twoHopTimes = new long[measuredIterations];
        long[] threeHopTimes = new long[measuredIterations];

        List<String> nodeIds = new ArrayList<>();

        try (Session session = driver.session()) {

            // Get actual node IDs from the database
            session.run(
                "MATCH (p:Person) RETURN p.id AS id"
            ).list(record -> record.get("id").asString())
             .forEach(nodeIds::add);

            if (nodeIds.isEmpty()) {
                System.out.println("No Person nodes found.");
                return;
            }

            Random random = new Random();

            // Warm-up
            for (int i = 0; i < warmupIterations; i++) {

                String id = nodeIds.get(
                    random.nextInt(nodeIds.size())
                );

                session.run(
                    "MATCH (p:Person {id:$id})" +
                    "-[:CONNECTED_TO*1..3]->(friend) " +
                    "RETURN friend",
                    Values.parameters("id", id)
                ).consume();
            }

            // 1-hop
            for (int i = 0; i < measuredIterations; i++) {

                String id = nodeIds.get(
                    random.nextInt(nodeIds.size())
                );

                long start = System.nanoTime();

                session.run(
                    "MATCH (p:Person {id:$id})" +
                    "-[:CONNECTED_TO*1]->(friend) " +
                    "RETURN friend",
                    Values.parameters("id", id)
                ).consume();

                long end = System.nanoTime();

                oneHopTimes[i] = end - start;
            }

            // 2-hop
            for (int i = 0; i < measuredIterations; i++) {

                String id = nodeIds.get(
                    random.nextInt(nodeIds.size())
                );

                long start = System.nanoTime();

                session.run(
                    "MATCH (p:Person {id:$id})" +
                    "-[:CONNECTED_TO*2]->(friend) " +
                    "RETURN friend",
                    Values.parameters("id", id)
                ).consume();

                long end = System.nanoTime();

                twoHopTimes[i] = end - start;
            }

            // 3-hop
            for (int i = 0; i < measuredIterations; i++) {

                String id = nodeIds.get(
                    random.nextInt(nodeIds.size())
                );

                long start = System.nanoTime();

                session.run(
                    "MATCH (p:Person {id:$id})" +
                    "-[:CONNECTED_TO*3]->(friend) " +
                    "RETURN friend",
                    Values.parameters("id", id)
                ).consume();

                long end = System.nanoTime();

                threeHopTimes[i] = end - start;
            }
        }

        double oneHopP50 = Statistics.p50(oneHopTimes);
        double oneHopP95 = Statistics.p95(oneHopTimes);

        double twoHopP50 = Statistics.p50(twoHopTimes);
        double twoHopP95 = Statistics.p95(twoHopTimes);

        double threeHopP50 = Statistics.p50(threeHopTimes);
        double threeHopP95 = Statistics.p95(threeHopTimes);

        System.out.println("--------------------------------");
        System.out.println("TRAVERSAL BENCHMARK");
        System.out.println("--------------------------------");

        System.out.println("Warm-up Iterations  : " + warmupIterations);
        System.out.println("Measured Iterations : " + measuredIterations);
        System.out.println("Available Node IDs  : " + nodeIds.size());

        System.out.println("1-Hop P50 : " + oneHopP50 + " ms");
        System.out.println("1-Hop P95 : " + oneHopP95 + " ms");

        System.out.println("2-Hop P50 : " + twoHopP50 + " ms");
        System.out.println("2-Hop P95 : " + twoHopP95 + " ms");

        System.out.println("3-Hop P50 : " + threeHopP50 + " ms");
        System.out.println("3-Hop P95 : " + threeHopP95 + " ms");

        CSVWriter.writeResult(
            "Traversal 1-Hop",
            oneHopP50,
            oneHopP95
        );

        CSVWriter.writeResult(
            "Traversal 2-Hop",
            twoHopP50,
            twoHopP95
        );

        CSVWriter.writeResult(
            "Traversal 3-Hop",
            threeHopP50,
            threeHopP95
        );
    }
}