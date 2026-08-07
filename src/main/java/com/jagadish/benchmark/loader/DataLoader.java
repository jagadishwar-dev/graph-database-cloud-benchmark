package com.jagadish.benchmark.loader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.Values;

import com.jagadish.benchmark.database.CognoDBConnection;

public class DataLoader {
	public void loadCSV(String filePath) {

	    Driver driver = CognoDBConnection.getDriver();

	    try (
	            Session session = driver.session();
	            BufferedReader br = new BufferedReader(new FileReader(filePath))
	    ) {

	        String line;

	        // Skip header
	        br.readLine();

	        int count = 0;

	        while ((line = br.readLine()) != null) {

	            String[] values = line.split(",");

	            String source = values[0];
	            String target = values[1];

	            session.run(
	                    "MERGE (a:Person {id:$source}) " +
	                    "MERGE (b:Person {id:$target}) " +
	                    "MERGE (a)-[:CONNECTED_TO]->(b)",
	                    Values.parameters(
	                            "source", source,
	                            "target", target
	                    )
	            );

	            count++;

	            if (count % 500 == 0) {
	                System.out.println("Inserted " + count + " relationships...");
	            }
	        }

	        System.out.println("Finished loading " + count + " relationships.");

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
 }