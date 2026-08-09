package com.jagadish.benchmark;

import com.jagadish.benchmark.database.CognoDBConnection;

public class ConnectionTest {

    public static void main(String[] args) {

        try {

            CognoDBConnection.getDriver();

            System.out.println(
                "SUCCESS: Connected to CognoDB!"
            );

        } catch (Exception e) {

            System.out.println(
                "FAILED: Could not connect to CognoDB."
            );

            e.printStackTrace();

        } finally {

            CognoDBConnection.closeConnection();
        }
    }
}