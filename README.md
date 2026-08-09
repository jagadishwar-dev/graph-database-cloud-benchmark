# Graph Database Cloud Benchmark

A Java-based benchmarking project for evaluating the performance of a cloud-hosted graph database using the POKEC social-network dataset.

The project measures database performance for data insertion, node lookup, graph traversal, aggregation queries, and mixed concurrent workloads.

---

## 1. Project Objective

The objective of this project is to benchmark a cloud graph database using a realistic social-network graph dataset and measure:

- Data insertion performance
- Node lookup latency
- 1-hop, 2-hop, and 3-hop graph traversal latency
- Aggregation query latency
- Concurrent mixed workload performance
- P50 and P95 query latency
- Throughput in operations per second

---

## 2. Technologies Used

- Java 17
- Maven
- Neo4j Java Driver
- CognoDB Cloud
- Cypher Query Language
- POKEC Dataset
- CSV
- Eclipse IDE

---

## 3. Dataset

The benchmark uses the POKEC social-network dataset.

The benchmark dataset contains:

| Metric | Value |
|---|---:|
| Unique Nodes | 49,683 |
| Relationships | 100,000 |

The relationships are represented using:

```text
(:Person)-[:CONNECTED_TO]->(:Person)

---

## 4. Project Structure

```text
graph-database-cloud-benchmark
│
├── src/main/java
│   ├── com.jagadish.benchmark
│   │   ├── Main.java
│   │   ├── GenerateDataset.java
│   │   ├── PokecDatasetConverter.java
│   │   ├── ConnectionTest.java
│   │   └── CypherTest.java
│   │
│   ├── com.jagadish.benchmark.benchmark
│   │   ├── InsertBenchmark.java
│   │   ├── LookupBenchmark.java
│   │   ├── TraversalBenchmark.java
│   │   ├── AggregationBenchmark.java
│   │   └── MixedWorkloadBenchmark.java
│   │
│   ├── com.jagadish.benchmark.config
│   │   └── DatabaseConfig.java
│   │
│   ├── com.jagadish.benchmark.database
│   │   └── CognoDBConnection.java
│   │
│   ├── com.jagadish.benchmark.loader
│   │   └── DataLoader.java
│   │
│   └── com.jagadish.benchmark.util
│       ├── CSVWriter.java
│       ├── Statistics.java
│       └── Timer.java
│
├── src/main/resources
│   └── dataset
│       └── graph.csv
│
├── results
│   └── benchmark.csv
│
├── pom.xml
├── README.md
└── .gitignore

---

## 5. Benchmark Workloads

### 5.1 Insert Benchmark

The insert benchmark loads the graph CSV dataset into the database.

Relationships are processed in batches of 500 using Cypher `UNWIND`.

The following operations are performed:

```cypher
UNWIND $relationships AS rel
MERGE (a:Person {id: rel.source})
MERGE (b:Person {id: rel.target})
MERGE (a)-[:CONNECTED_TO]->(b)

---

## 6. Benchmark Methodology

### Warm-up Iterations

Warm-up queries are executed before the measured queries to reduce the effect of initial JVM and database overhead.

### P50 Latency

P50 represents the median latency.

Approximately 50% of the measured operations complete within this time.

### P95 Latency

P95 represents the latency below which approximately 95% of measured operations complete.

P95 is useful for understanding slower requests and tail latency.

### Measurement

Query execution time is measured using Java's:

```java
System.nanoTime()

---

## 7. Benchmark Results

The benchmark was executed using:

| Metric | Value |
|---|---:|
| Nodes | 49,683 |
| Relationships | 100,000 |

### Insert Benchmark

| Metric | Result |
|---|---:|
| Nodes | 49,683 |
| Relationships | 100,000 |
| Load Time | 2,782,726 ms |
| Nodes/sec | 17.854 |
| Relationships/sec | 35.936 |

### Lookup Benchmark

| Metric | Result |
|---|---:|
| P50 | 309.0 ms |
| P95 | 635.1822 ms |

### Traversal Benchmark

| Traversal | P50 (ms) | P95 (ms) |
|---|---:|---:|
| 1-Hop | 319.2773 | 639.341 |
| 2-Hop | 319.5801 | 740.084 |
| 3-Hop | 301.0503 | 425.3072 |

### Aggregation Benchmark

| Metric | Result |
|---|---:|
| P50 | 295.3457 ms |
| P95 | 357.7538 ms |

### Mixed Workload Benchmark

| Metric | Result |
|---|---:|
| Concurrent Clients | 10 |
| Operations/Client | 20 |
| Total Operations | 200 |
| P50 | 303.7339 ms |
| P95 | 1486.1186 ms |
| Total Time | 10.3298 seconds |
| Throughput | 19.36 operations/sec |

---

## 8. Results Summary

The benchmark demonstrates the performance characteristics of the graph database across different workloads.

The insert workload took approximately 2,782.7 seconds to load 100,000 relationships, with a throughput of approximately 35.94 relationships per second.

The lookup benchmark achieved a P50 latency of 309.0 ms and a P95 latency of 635.18 ms.

For graph traversal, the benchmark measured 1-hop, 2-hop, and 3-hop traversal performance separately.

The aggregation benchmark achieved a P50 latency of 295.35 ms and a P95 latency of 357.75 ms.

The mixed workload used 10 concurrent clients and achieved approximately 19.36 operations per second, with a P95 latency of approximately 1.49 seconds.

---

## 9. Configuration

Database connection details are provided through environment variables.

The application expects:

```text
COGNODB_URI
COGNODB_USERNAME
COGNODB_PASSWORD

---

## 10. Running the Project

### Step 1: Configure Environment Variables

Set the required CognoDB connection environment variables:

```text
COGNODB_URI
COGNODB_USERNAME
COGNODB_PASSWORD

---

## 11. Output

The benchmark results are stored in:

```text
results/benchmark.csv

---

## 12. Important Note

The insert benchmark processes 100,000 relationships and may take significant time depending on network latency, cloud database performance, batching configuration, and database load.

The benchmark should not be repeatedly executed unnecessarily when validating project code.

---

## 13. Future Improvements

Possible improvements include:

- Testing larger datasets
- Testing different batch sizes
- Testing different concurrency levels
- Comparing indexed and non-indexed lookup performance
- Measuring database-side execution time separately from network latency
- Adding more graph traversal patterns
- Adding automated benchmark reports
- Adding charts for P50, P95, and throughput
- Comparing different graph database configurations

---

## 14. Conclusion

This project provides a Java-based benchmark framework for evaluating cloud graph database performance using a social-network graph dataset.

It measures insertion throughput, node lookup latency, graph traversal latency, aggregation performance, and concurrent mixed workloads.

The collected benchmark results provide quantitative information for understanding database behavior under different workload patterns.