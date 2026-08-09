package com.jagadish.benchmark.benchmark;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.Values;

import com.jagadish.benchmark.database.CognoDBConnection;
import com.jagadish.benchmark.util.CSVWriter;
import com.jagadish.benchmark.util.Statistics;

public class MixedWorkloadBenchmark {

    public void runBenchmark() {

        Driver driver = CognoDBConnection.getDriver();

        int clients = 10;
        int operationsPerClient = 20;

        /*
         * Each client performs 20 operations:
         *
         * 7  Lookup       = 35%
         * 7  Aggregation  = 35%
         * 4  Traversal    = 20%
         * 2  Write        = 10%
         *
         * Total:
         * 70% reads
         * 20% traversal
         * 10% writes
         */

        ExecutorService executor =
                Executors.newFixedThreadPool(clients);

        List<Callable<List<Long>>> tasks =
                new ArrayList<>();

        /*
         * Get actual node IDs from the database.
         */
        List<String> nodeIds = new ArrayList<>();

        try (Session session = driver.session()) {

            session.run(
                "MATCH (p:Person) RETURN p.id AS id"
            ).list(record ->
                record.get("id").asString()
            ).forEach(nodeIds::add);
        }

        if (nodeIds.isEmpty()) {

            System.out.println(
                "No Person nodes found."
            );

            executor.shutdown();
            return;
        }

        /*
         * Create tasks for concurrent clients.
         */
        for (int i = 0; i < clients; i++) {

            tasks.add(() -> {

                List<Long> times = new ArrayList<>();

                Random random = new Random();

                try (Session session = driver.session()) {

                    for (int j = 0;
                         j < operationsPerClient;
                         j++) {

                        String id =
                            nodeIds.get(
                                random.nextInt(
                                    nodeIds.size()
                                )
                            );

                        long start =
                            System.nanoTime();

                        /*
                         * 0 - 6
                         * 7 lookup operations
                         * 35%
                         */
                        if (j < 7) {

                            session.run(
                                "MATCH (p:Person {id:$id}) " +
                                "RETURN p",
                                Values.parameters(
                                    "id", id
                                )
                            ).consume();
                        }

                        /*
                         * 7 - 13
                         * 7 aggregation operations
                         * 35%
                         */
                        else if (j < 14) {

                            session.run(
                                "MATCH (p:Person) " +
                                "RETURN count(p)"
                            ).consume();
                        }

                        /*
                         * 14 - 17
                         * 4 traversal operations
                         * 20%
                         */
                        else if (j < 18) {

                            session.run(
                                "MATCH (p:Person {id:$id})" +
                                "-[:CONNECTED_TO*1..3]->(f) " +
                                "RETURN f",
                                Values.parameters(
                                    "id", id
                                )
                            ).consume();
                        }

                        /*
                         * 18 - 19
                         * 2 write operations
                         * 10%
                         */
                        else {

                            String source =
                                nodeIds.get(
                                    random.nextInt(
                                        nodeIds.size()
                                    )
                                );

                            String target =
                                nodeIds.get(
                                    random.nextInt(
                                        nodeIds.size()
                                    )
                                );

                            session.run(
                                "MERGE (a:Person {id:$source}) " +
                                "MERGE (b:Person {id:$target}) " +
                                "MERGE (a)-[:CONNECTED_TO]->(b)",
                                Values.parameters(
                                    "source", source,
                                    "target", target
                                )
                            ).consume();
                        }

                        long end =
                            System.nanoTime();

                        times.add(end - start);
                    }
                }

                return times;
            });
        }

        long startTime =
            System.nanoTime();

        List<Long> allTimes =
            new ArrayList<>();

        try {

            List<Future<List<Long>>> results =
                executor.invokeAll(tasks);

            for (Future<List<Long>> result : results) {

                allTimes.addAll(
                    result.get()
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            executor.shutdown();
        }

        long endTime =
            System.nanoTime();

        double totalSeconds =
            (endTime - startTime)
            / 1_000_000_000.0;

        long[] times =
            new long[allTimes.size()];

        for (int i = 0;
             i < allTimes.size();
             i++) {

            times[i] = allTimes.get(i);
        }

        double p50 =
            Statistics.p50(times);

        double p95 =
            Statistics.p95(times);

        int totalOperations =
            clients * operationsPerClient;

        double throughput =
            totalOperations / totalSeconds;

        System.out.println("--------------------------------");
        System.out.println("MIXED WORKLOAD BENCHMARK");
        System.out.println("--------------------------------");

        System.out.println(
            "Concurrent Clients : "
            + clients
        );

        System.out.println(
            "Operations/Client  : "
            + operationsPerClient
        );

        System.out.println(
            "Total Operations   : "
            + totalOperations
        );

        System.out.println(
            "Workload           : "
            + "70% Reads / 20% Traversal / 10% Writes"
        );

        System.out.println(
            "Available Node IDs : "
            + nodeIds.size()
        );

        System.out.println(
            "P50 Latency        : "
            + p50
            + " ms"
        );

        System.out.println(
            "P95 Latency        : "
            + p95
            + " ms"
        );

        System.out.println(
            "Total Time         : "
            + totalSeconds
            + " seconds"
        );

        System.out.println(
            "Throughput         : "
            + throughput
            + " operations/sec"
        );

        CSVWriter.writeResult(
            "Mixed Workload Benchmark",
            p50,
            p95
        );
    }
}