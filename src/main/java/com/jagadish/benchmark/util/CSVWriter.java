package com.jagadish.benchmark.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CSVWriter {

    private static final String FILE_PATH = "results/benchmark.csv";

    public static void writeResult(String benchmark,
                                   long milliseconds,
                                   double seconds) {

        try {

            File file = new File(FILE_PATH);

            boolean newFile = !file.exists();

            FileWriter writer = new FileWriter(file, true);

            if (newFile) {
                writer.append("Benchmark,Execution Time (ms),Execution Time (sec)\n");
            }

            writer.append(benchmark)
                  .append(",")
                  .append(String.valueOf(milliseconds))
                  .append(",")
                  .append(String.valueOf(seconds))
                  .append("\n");

            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}