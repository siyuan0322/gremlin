package org.example;

import com.alibaba.graphscope.groot.sdk.GrootClient;
import org.apache.tinkerpop.gremlin.driver.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

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
        System.out.println("yy");
    }
    public void query() {
        ResultSet results = gremlinClient.submit("g.V().limit(3).valueMap()");
        for (Result result : results) {
            System.out.println(result.getObject());
        }
        System.out.println("xx");
    }

    public void getSchema() {
        System.out.println(grootClient.getSchema());
    }

//    public void createSchema() throws IOException {
//        String path = "/Users/siyuan/CLionProjects/graphscope/interactive_engine/groot-client/src/test/resources/schema.json";
//        String json = new String(Files.readAllBytes(Path.of(path)), StandardCharsets.UTF_8);
//        grootClient.loadJsonSchema(json);
//    }
//
//    private void testAddVerticesEdges() {
//        for (int i = 0; i < 10; ++i) {
//            Map<String, String> properties = new HashMap<>();
//            properties.put("id", String.valueOf(i));
//            properties.put("name", "person-" + i);
//            properties.put("age", String.valueOf(i + 20));
//            grootClient.addVertex("person", properties);
//
//            properties.clear();
//            properties.put("id", String.valueOf(i));
//            properties.put("name", "software-" + i);
//            properties.put("lang", String.valueOf(i + 200));
//            grootClient.addVertex("software", properties);
//        }
//
//        for (int i = 0; i < 10; ++i) {
//            Map<String, String> srcPk = new HashMap<>();
//            Map<String, String> dstPk = new HashMap<>();
//            Map<String, String> properties = new HashMap<>();
//
//            srcPk.put("id", String.valueOf(i));
//            dstPk.put("id", String.valueOf(i));
//            properties.put("weight", String.valueOf(i * 100));
//            grootClient.addEdge("created", "person", "software", srcPk, dstPk, properties);
//        }
//        long snapshotId = grootClient.commit();
//        grootClient.remoteFlush(snapshotId);
//        System.out.println("Finished adding vertices and edges");
//    }
//
//    private void testUpdateDeleteEdge() {
//        Map<String, String> srcPk = new HashMap<>();
//        Map<String, String> dstPk = new HashMap<>();
//        Map<String, String> properties = new HashMap<>();
//
//        srcPk.put("id", String.valueOf(0));
//        dstPk.put("id", String.valueOf(0));
//        properties.put("weight", String.valueOf(10000));
//        grootClient.updateEdge("created", "person", "software", srcPk, dstPk, properties);
//        long snapshotId = grootClient.commit();
//        grootClient.remoteFlush(snapshotId);
//        System.out.println("Finished update edge person-0 -> software-0");
//
//        grootClient.deleteEdge("created", "person", "software", srcPk, dstPk);
//        snapshotId = grootClient.commit();
//        grootClient.remoteFlush(snapshotId);
//        System.out.println("Finished delete edge person-0 -> software-0");
//    }
//
//    private void testUpdateDeleteVertex() {
//        Map<String, String> properties = new HashMap<>();
//        properties.put("id", String.valueOf(0));
//        properties.put("name", "marko-0-updated");
//        grootClient.updateVertex("person", properties);
//        grootClient.remoteFlush(snapshotId);
//        System.out.println("Finished update vertex person-0");
//
//        Map<String, String> pk_properties = new HashMap<>();
//        grootClient.deleteVertex("person", pk_properties);
//        grootClient.remoteFlush(snapshotId);
//        System.out.println("Finished delete vertex person-0");
//    }

    public static void main(String[] args) throws IOException {
        String host = "47.57.139.88";
        int grpcPort = 55556;
        int gremlinPort = 12312;
        Main main = new Main(host, grpcPort, gremlinPort);
        main.getSchema();
//        main.createSchema();
//        main.testAddVerticesEdges();
//        main.testUpdateDeleteEdge();
//        main.testUpdateDeleteVertex();
//        main.query();
//        main.close();
        System.out.println("zz");
        System.exit(0);
    }
}
