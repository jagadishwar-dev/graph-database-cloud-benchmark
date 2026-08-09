package com.jagadish.benchmark.loader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.Value;
import org.neo4j.driver.Values;

import com.jagadish.benchmark.database.CognoDBConnection;

public class DataLoader {

    public static class LoadResult {

        private final int nodes;
        private final int relationships;

        public LoadResult(int nodes, int relationships) {
            this.nodes = nodes;
            this.relationships = relationships;
        }

        public int getNodes() {
            return nodes;
        }

        public int getRelationships() {
            return relationships;
        }
    }

    public LoadResult loadCSV(String filePath) {

        int relationshipCount = 0;

        // Smaller batches reduce the chance of long-running transactions
        int batchSize = 500;

        Set<String> nodes = new HashSet<>();

        Driver driver = CognoDBConnection.getDriver();

        try (
            Session session = driver.session();
            BufferedReader br =
                new BufferedReader(new FileReader(filePath))
        ) {

            String line;

            // Skip CSV header
            br.readLine();

            List<Value> relationships =
                new ArrayList<>();

            while ((line = br.readLine()) != null) {

                String[] values =
                    line.split(",");

                if (values.length < 2) {
                    continue;
                }

                String source =
                    values[0].trim();

                String target =
                    values[1].trim();

                if (source.isEmpty() || target.isEmpty()) {
                    continue;
                }

                nodes.add(source);
                nodes.add(target);

                relationships.add(
                    Values.parameters(
                        "source", source,
                        "target", target
                    )
                );

                if (relationships.size() >= batchSize) {

                    insertBatch(
                        session,
                        relationships
                    );

                    relationshipCount +=
                        relationships.size();

                    System.out.println(
                        "Inserted "
                        + relationshipCount
                        + " relationships..."
                    );

                    relationships.clear();
                }
            }

            // Insert final batch
            if (!relationships.isEmpty()) {

                insertBatch(
                    session,
                    relationships
                );

                relationshipCount +=
                    relationships.size();

                System.out.println(
                    "Inserted "
                    + relationshipCount
                    + " relationships..."
                );
            }

        } catch (IOException e) {

            System.out.println(
                "Error reading dataset:"
            );

            e.printStackTrace();
        }

        return new LoadResult(
            nodes.size(),
            relationshipCount
        );
    }

    private void insertBatch(
            Session session,
            List<Value> relationships) {

        try (Transaction tx =
                session.beginTransaction()) {

            tx.run(
                "UNWIND $relationships AS rel " +
                "MERGE (a:Person {id: rel.source}) " +
                "MERGE (b:Person {id: rel.target}) " +
                "MERGE (a)-[:CONNECTED_TO]->(b)",
                Values.parameters(
                    "relationships",
                    relationships
                )
            ).consume();

            tx.commit();
        }
    }
}