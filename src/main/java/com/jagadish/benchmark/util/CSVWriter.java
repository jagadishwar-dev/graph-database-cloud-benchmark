package com.jagadish.benchmark.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CSVWriter {

    private static final String FILE_PATH =
            "results/benchmark.csv";

    // For normal benchmarks
    public static void writeResult(
            String benchmark,
            double p50,
            double p95) {

        try {

            File file = new File(FILE_PATH);

            File parent = file.getParentFile();

            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }

            boolean newFile = !file.exists();

            try (FileWriter writer =
                    new FileWriter(file, true)) {

                if (newFile) {
                    writer.append(
                        "Benchmark," +
                        "P50 Latency (ms)," +
                        "P95 Latency (ms)," +
                        "Nodes," +
                        "Relationships," +
                        "Load Time (ms)," +
                        "Nodes/sec," +
                        "Relationships/sec\n"
                    );
                }

                writer.append(benchmark)
                      .append(",")
                      .append(String.valueOf(p50))
                      .append(",")
                      .append(String.valueOf(p95))
                      .append(",,,,,\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // For Insert Benchmark
    public static void writeIngestResult(
            int nodes,
            int relationships,
            double loadTime,
            double nodesPerSecond,
            double relationshipsPerSecond) {

        try {

            File file = new File(FILE_PATH);

            File parent = file.getParentFile();

            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }

            boolean newFile = !file.exists();

            try (FileWriter writer =
                    new FileWriter(file, true)) {

                if (newFile) {
                    writer.append(
                        "Benchmark," +
                        "P50 Latency (ms)," +
                        "P95 Latency (ms)," +
                        "Nodes," +
                        "Relationships," +
                        "Load Time (ms)," +
                        "Nodes/sec," +
                        "Relationships/sec\n"
                    );
                }

                writer.append("Insert Benchmark")
                      .append(",N/A")
                      .append(",N/A")
                      .append(",")
                      .append(String.valueOf(nodes))
                      .append(",")
                      .append(String.valueOf(relationships))
                      .append(",")
                      .append(String.valueOf(loadTime))
                      .append(",")
                      .append(String.valueOf(nodesPerSecond))
                      .append(",")
                      .append(String.valueOf(relationshipsPerSecond))
                      .append("\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}