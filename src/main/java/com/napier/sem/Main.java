package com.napier.sem;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.net.Socket;

public class Main
{
    public static void main(String[] args)
    {
        // Disable MongoDB driver logging to reduce noise
        Logger.getLogger("org.mongodb").setLevel(Level.WARNING);
        Logger.getLogger("com.mongodb").setLevel(Level.WARNING);
        
        // Get MongoDB host from environment variable, default to mongo-dbserver
        String mongoHost = System.getenv("MONGO_HOST");
        if (mongoHost == null || mongoHost.isEmpty()) {
            mongoHost = "mongo-dbserver";
        }
        
        // Get MongoDB port from environment variable, default to 27017
        String mongoPortStr = System.getenv("MONGO_PORT");
        int mongoPort = 27017;
        if (mongoPortStr != null && !mongoPortStr.isEmpty()) {
            try {
                mongoPort = Integer.parseInt(mongoPortStr);
            } catch (NumberFormatException e) {
                System.out.println("Invalid MONGO_PORT, using default 27017");
            }
        }
        
        System.out.println("Connecting to MongoDB at " + mongoHost + ":" + mongoPort);
        
        // First, quickly test if the host is reachable
        if (!isHostReachable(mongoHost, mongoPort)) {
            System.out.println("❌ MongoDB server not found at " + mongoHost + ":" + mongoPort);
            System.out.println("💡 To run this application, you need MongoDB running. Try:");
            System.out.println("   • docker-compose up -d  (recommended)");
            System.out.println("   • ./run-standalone.sh 2  (custom network)");
            System.out.println("   • ./run-standalone.sh 3  (localhost connection)");
            System.exit(1);
        }
        
        // Connect to MongoDB with retry logic
        MongoClient mongoClient = null;
        int maxRetries = 3;
        int retryCount = 0;
        
        while (mongoClient == null && retryCount < maxRetries) {
            try {
                // Create MongoDB client with shorter timeout settings
                MongoClientOptions options = MongoClientOptions.builder()
                    .connectTimeout(5000)  // 5 seconds
                    .socketTimeout(5000)   // 5 seconds
                    .serverSelectionTimeout(5000)  // 5 seconds
                    .build();
                    
                mongoClient = new MongoClient(new ServerAddress(mongoHost, mongoPort), options);
                
                // Test the connection
                mongoClient.getDatabase("admin").runCommand(new Document("ping", 1));
                System.out.println("✅ Successfully connected to MongoDB!");
                break;
            } catch (Exception e) {
                retryCount++;
                
                if (retryCount == 1) {
                    System.out.println("⏳ Waiting for MongoDB to become available...");
                } else {
                    System.out.println("⚠️  Still waiting... (attempt " + retryCount + "/" + maxRetries + ")");
                }
                
                if (retryCount >= maxRetries) {
                    System.err.println("❌ Could not connect to MongoDB after " + maxRetries + " attempts.");
                    System.err.println("💡 Make sure MongoDB is running and accessible at " + mongoHost + ":" + mongoPort);
                    System.err.println("   Use 'docker-compose up -d' for the easiest setup.");
                    System.exit(1);
                }
                
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                }
                
                // Close the failed client to avoid resource leaks
                if (mongoClient != null) {
                    try {
                        mongoClient.close();
                    } catch (Exception ignored) {}
                    mongoClient = null;
                }
            }
        }
        
        // Get a database - will create when we use it
        MongoDatabase database = mongoClient.getDatabase("mydb");
        // Get a collection from the database
        MongoCollection<Document> collection = database.getCollection("test");
        // Create a document to store
        Document doc = new Document("name", "Kevin Sim")
                .append("class", "Software Engineering Methods")
                .append("year", "2021")
                .append("result", new Document("CW", 95).append("EX", 85));
        // Add document to collection
        collection.insertOne(doc);

        // Check document in collection
        Document myDoc = collection.find().first();
        System.out.println(myDoc.toJson());
    }
    
    // Helper method to quickly check if host is reachable
    private static boolean isHostReachable(String host, int port) {
        try (Socket socket = new Socket()) {
            socket.connect(new java.net.InetSocketAddress(host, port), 2000);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}