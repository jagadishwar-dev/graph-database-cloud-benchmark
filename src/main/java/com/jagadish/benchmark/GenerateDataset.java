package com.jagadish.benchmark;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;

public class GenerateDataset {

    public static void main(String[] args) {

        String filePath = "src/main/resources/dataset/graph.csv";

        Set<String> nodes = new HashSet<>();
        int relationships = 0;

        try (BufferedReader reader = new BufferedReader(
                new FileReader(filePath))) {

            // Skip header
            reader.readLine();

            String line;

            while ((line = reader.readLine()) != null) {

                String[] values = line.split(",");

                if (values.length < 2) {
                    continue;
                }

                String source = values[0].trim();
                String target = values[1].trim();

                nodes.add(source);
                nodes.add(target);

                relationships++;
            }

            System.out.println("--------------------------------");
            System.out.println("POKEC DATASET STATISTICS");
            System.out.println("--------------------------------");
            System.out.println("Unique Nodes  : " + nodes.size());
            System.out.println("Relationships : " + relationships);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}