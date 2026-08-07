package com.jagadish.benchmark;

import java.io.FileWriter;
import java.io.IOException;

public class GenerateDataset {

    public static void main(String[] args) {

        String filePath = "src/main/resources/dataset/graph.csv";

        int nodes = 2000;
        int relationships = 4000;

        try (FileWriter writer = new FileWriter(filePath)) {

            int source = 1;
            int target = 2;

            for (int i = 0; i < relationships; i++) {

                writer.write(source + "," + target + "\n");

                target++;

                if (target > nodes) {
                    source++;
                    target = source + 1;
                }

                if (source >= nodes) {
                    source = 1;
                    target = 2;
                }
            }

            System.out.println("Dataset generated successfully!");
            System.out.println("Nodes: " + nodes);
            System.out.println("Relationships: " + relationships);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}