package com.jagadish.benchmark;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;

import com.jagadish.benchmark.database.CognoDBConnection;

public class CypherTest {

    public static void main(String[] args) {

        Driver driver = null;

        try {
            driver = CognoDBConnection.getDriver();

            try (Session session = driver.session()) {

                var result = session.run(
                    "RETURN 1 AS test"
                );

                var record = result.single();

                System.out.println(
                    "Cypher query result: " + record.get("test").asInt()
                );
            }

        } catch (Exception e) {

            System.out.println("Cypher query failed.");
            e.printStackTrace();

        } finally {

            CognoDBConnection.closeConnection();
        }
    }
}