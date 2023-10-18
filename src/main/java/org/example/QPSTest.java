package org.example;
import org.apache.tinkerpop.gremlin.driver.Client;
import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.driver.Result;
import org.apache.tinkerpop.gremlin.driver.ResultSet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class QPSTest {

    public static void main(String[] args) throws InterruptedException, IOException {
        // Create a Gremlin cluster
        Cluster cluster = Cluster.build()
                .addContactPoint("localhost")
                .port(8182)
                .create();
        int threadNum = 1;
        // Read queries from file
        List<String> queries = readQueriesFromFile("query.txt");
        long[][] latencies = new long[threadNum][queries.size()];

        // Create an array of threads
        Thread[] threads = new Thread[threadNum];

        // Start the threads
        for (int i = 0; i < threadNum; i++) {
            int finalI = i;
            threads[i] = new Thread(() -> {
                // Create a Gremlin client for each thread
                Client client = cluster.connect();

                // Send queries and collect latency
                for (int j = 0; j < queries.size(); ++j) {
                    long startTime = System.currentTimeMillis();

                    CompletableFuture<ResultSet> future = client.submitAsync(queries.get(j));

                    try {
                        ResultSet results = future.get();

                        // Process the results
                        for (Result ignored : results) {
//                            Vertex vertex = result.getVertex();
//                            System.out.println("Vertex: " + vertex);
                        }

                        long endTime = System.currentTimeMillis();
                        long latency = endTime - startTime;

                        latencies[finalI][j] = latency;
                        Thread.sleep(500);
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }

                // Close the Gremlin client
                client.close();
            });

            // Start the thread
            threads[i].start();
        }

        // Wait for all threads to finish
        for (Thread thread : threads) {
            thread.join();
        }

        // Print the query latency table
        for (int i = 0; i < threadNum; ++i) {
            Metrics.printStatistics(latencies[i]);
        }

        // Close the Gremlin cluster
        cluster.close();
    }

    private static List<String> readQueriesFromFile(String filePath) throws IOException {
        List<String> queries = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;

        while ((line = reader.readLine()) != null) {
            queries.add(line);
        }

        reader.close();
        return queries;
    }
}

