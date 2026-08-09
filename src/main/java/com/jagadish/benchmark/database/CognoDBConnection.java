package com.jagadish.benchmark.database;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;

import com.jagadish.benchmark.config.DatabaseConfig;

public class CognoDBConnection {

    private static Driver driver;

    public static Driver getDriver() {

        if (driver == null) {

            driver = GraphDatabase.driver(
                    DatabaseConfig.URI,
                    AuthTokens.basic(
                            DatabaseConfig.USERNAME,
                            DatabaseConfig.PASSWORD));

            driver.verifyConnectivity();

            System.out.println("Connected to CognoDB successfully!");
        }

        return driver;
    }

    public static void closeConnection() {

        if (driver != null) {

            driver.close();
            driver = null;

            System.out.println("Connection Closed.");
        }
    }
}