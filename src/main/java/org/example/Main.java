package org.example;

import com.alibaba.graphscope.groot.sdk.BasicAuth;
import com.alibaba.graphscope.groot.sdk.GrootClient;
import com.alibaba.graphscope.proto.groot.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.apache.tinkerpop.gremlin.driver.*;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Main {

    private final GrootClient grootClient;
    private final Client gremlinClient;
    public Main(String host, int grpcPort, int gremlinPort) {
        Cluster.Builder builder = Cluster.build();
        builder.addContactPoint(host);
        builder.port(gremlinPort);
        // Set the username and password if necessary
        // builder.credentials("admin", "admin");
        // builder.enableSsl(true);
        Cluster cluster = builder.create();
        grootClient = GrootClient.newBuilder().addHost(host, grpcPort).build();
        gremlinClient = cluster.connect();
    }

    public void close() {
        if (gremlinClient != null) {
            gremlinClient.close();
        }
    }
    public void query(String query) {
        ResultSet results = gremlinClient.submit(query);
        for (Result result : results) {
            System.out.println(result.getObject());
        }
    }

    public void testQueryAsync() {
        while (true) {
            try {
                queryAsync();
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                break;
            }
        }
    }
    public void queryAsync() throws InterruptedException {
        try {
            ResultSet results = gremlinClient.submitAsync("g.V().limit(1)").get();
        } catch (ExecutionException ex) {
            System.out.println(ex);
        }
    }

    public void getSchema() {
        System.out.println(grootClient.getSchema());
    }

    public void createSchema() throws IOException {
        String path = "/Users/siyuan/CLionProjects/graphscope/interactive_engine/groot-client/src/test/resources/schema.json";
        String json = new String(Files.readAllBytes(Path.of(path)), StandardCharsets.UTF_8);
        grootClient.loadJsonSchema(json);
    }


    public static void main(String[] args) throws IOException {
        String host = "47.57.139.88";
        int grpcPort = 55556;
        int gremlinPort = 12312;
        Main main = new Main(host, grpcPort, gremlinPort);
        main.getSchema();
//        main.createSchema();
        main.query("g.V().limit(3).valueMap()");
        main.close();

        System.exit(0);
    }


}
