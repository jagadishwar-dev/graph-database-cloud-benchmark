package com.jagadish.benchmark;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class PokecDatasetConverter {

    private static final int TARGET_RELATIONSHIPS = 100_000;

    private static final String INPUT_FILE =
            "C:/pokec/soc-pokec-relationships.txt/soc-pokec-relationships";

    private static final String OUTPUT_FILE =
            "src/main/resources/dataset/graph.csv";

    public static void main(String[] args) {

        int relationshipCount = 0;

        try (
            BufferedReader reader = new BufferedReader(
                    new FileReader(INPUT_FILE));

            BufferedWriter writer = new BufferedWriter(
                    new FileWriter(OUTPUT_FILE))
        ) {

            writer.write("source,target");
            writer.newLine();

            String line;

            while ((line = reader.readLine()) != null
                    && relationshipCount < TARGET_RELATIONSHIPS) {

                line = line.trim();

                // Ignore empty lines
                if (line.isEmpty()) {
                    continue;
                }

                // SNAP Pokec uses TAB-separated source and target IDs
                String[] parts = line.split("\\s+");

                if (parts.length < 2) {
                    continue;
                }

                String source = parts[0];
                String target = parts[1];

                writer.write(source);
                writer.write(",");
                writer.write(target);
                writer.newLine();

                relationshipCount++;
            }

            System.out.println("--------------------------------");
            System.out.println("POKEC DATASET CONVERSION");
            System.out.println("--------------------------------");
            System.out.println("Relationships created : "
                    + relationshipCount);
            System.out.println("Output file           : "
                    + OUTPUT_FILE);

        } catch (IOException e) {
            System.out.println("Error processing dataset:");
            e.printStackTrace();
        }
    }
}