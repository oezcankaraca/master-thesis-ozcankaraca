import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.GsonBuilder;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Random;

/**
 * The GeneratorOfNetworkTopologyRandom class is used to generate a random network topology
 * and save it as a JSON file.
 * It simulates network parameters such as upload and download speeds for a set of peers in the network.
 * 
 * @author Özcan Karaca
 */
public class GeneratorOfNetworkTopologyRandom {
    // Number of peers in the network
    private static int numberOfPeers = 50;
    // Random object for generating random values
    private static final Random random = new Random();
    // Mean upload and download speeds
    private static final double meanUploadDe = 30;
    private static final double meanDownloadDe = 90; 
    // Variance of upload and download speeds
    private static final double varianceUploadEn = 93.0; 
    private static final double varianceDownloadEn = 9959.0;
    // Standard deviation, calculated from the variance
    private static final double stdDevUploadEn = Math.sqrt(varianceUploadEn);
    private static final double stdDevDownloadEn = Math.sqrt(varianceDownloadEn);

    // JSON arrays to hold peer and connection data
    static JsonArray peersArray = new JsonArray();
    static JsonArray connectionsArray = new JsonArray();

    // Lists to hold upload and download speeds of peers
    static ArrayList<Double> uploadSpeeds = new ArrayList<>();
    static ArrayList<Double> downloadSpeeds = new ArrayList<>();

    /**
     * Main method to generate network data and save it as a JSON file.
     */
    public static void main(String[] args) {
        generateNetworkData();
        saveJson();
    }

     /**
     * Generates network data including peers and their upload and download speeds.
     */
    private static void generateNetworkData() {
        // Generate upload and download speeds for each peer
        for (int i = 1; i <= numberOfPeers; i++) {
            double[] speeds = generateSpeeds();
            uploadSpeeds.add(speeds[0]);
            downloadSpeeds.add(speeds[1]);
        }

        // Find the maximum upload and download speeds
        double maxUpload = Collections.max(uploadSpeeds);
        double maxDownload = Collections.max(downloadSpeeds);

        // Add a special peer representing a lecture studio server
        addLectureStudioServerPeer(maxUpload, maxDownload);

        // Add each peer with their respective speeds to the JSON array
        for (int i = 0; i < numberOfPeers; i++) {
            addPeer(i + 1, uploadSpeeds.get(i), downloadSpeeds.get(i));
        }

        // Generate network connections
        generateConnections();
    }

    /**
     * Generates random upload and download speeds based on a normal distribution.
     * 
     * @return A double array containing two elements: upload speed and download speed.
     */
    private static double[] generateSpeeds() {
        double uploadSpeed, downloadSpeed;
    
        do {
            uploadSpeed = -1;
            downloadSpeed = -1;
    
            while (uploadSpeed <= 0) {
                uploadSpeed = meanUploadDe + stdDevUploadEn * random.nextGaussian();
            }
    
            while (downloadSpeed <= uploadSpeed) {
                downloadSpeed = meanDownloadDe + stdDevDownloadEn * random.nextGaussian();
            }
    
            uploadSpeed = Math.round(uploadSpeed * 1000);
            downloadSpeed = Math.round(downloadSpeed * 1000);
        } while (uploadSpeed >= downloadSpeed);
    
        return new double[]{uploadSpeed, downloadSpeed};
    }
    

     /**
     * Adds the lectureStudio server with maximum upload and download speeds.
     * 
     * @param upload The maximum upload speed for the lecture studio server.
     * @param download The maximum download speed for the lecture studio server.
     */
    private static void addLectureStudioServerPeer(double upload, double download) {
        JsonObject lectureStudioServer = new JsonObject();
        lectureStudioServer.addProperty("name", "lectureStudioServer");
        lectureStudioServer.addProperty("maxUpload", (int) upload);
        lectureStudioServer.addProperty("maxDownload", (int) download);
        peersArray.add(lectureStudioServer);
    }

    /**
     * Adds a peer with given ID and upload/download speeds to the JSON array.
     * 
     * @param peerId The ID of the peer.
     * @param upload The upload speed of the peer.
     * @param download The download speed of the peer.
     */
    private static void addPeer(int peerId, double upload, double download) {
        JsonObject peerObject = new JsonObject();
        peerObject.addProperty("name", String.valueOf(peerId));
        peerObject.addProperty("maxUpload", (int) upload);
        peerObject.addProperty("maxDownload", (int) download);
        peersArray.add(peerObject);
    }

    /**
     * Generates network connections between peers and adds them to the JSON array.
     * The connections include parameters like latency, packet loss, and bandwidth.
     */
    private static void generateConnections() {
        for (int i = 0; i < peersArray.size(); i++) {
            for (int j = 0; j < peersArray.size(); j++) {
                if (i != j) {
                    JsonObject connection = new JsonObject();
                    JsonObject sourcePeer = peersArray.get(i).getAsJsonObject();
                    JsonObject targetPeer = peersArray.get(j).getAsJsonObject();

                    // Gather connection parameters
                    int sourceUpload = sourcePeer.get("maxUpload").getAsInt();
                    int targetDownload = targetPeer.get("maxDownload").getAsInt();
                    double latency = 40 + random.nextDouble() * 40;
                    double packetLoss = 0.001 + random.nextDouble() * 0.001;
                    int bandwidth = Math.min(sourceUpload, targetDownload);

                    // Add connection details to the JSON object
                    connection.addProperty("sourceName", sourcePeer.get("name").getAsString());
                    connection.addProperty("targetName", targetPeer.get("name").getAsString());
                    connection.addProperty("bandwidth", bandwidth);
                    connection.addProperty("latency", String.format(Locale.US, "%.2f", latency));
                    connection.addProperty("loss", String.format(Locale.US, "%.4f", packetLoss));

                    connectionsArray.add(connection);
                }
            }
        }
    }

     /**
     * Saves the generated network topology data as a JSON file.
     * 
     * The JSON file includes details about peers and their connections.
     */
    private static void saveJson() {
        String homeDirectory = System.getProperty("user.home");
        String basePath = homeDirectory + "/Desktop/master-thesis-ozcankaraca";
        String pathToInputData = basePath + "/data-for-testbed/inputs-new/input-data-" + numberOfPeers + ".json";

        JsonObject networkTopology = new JsonObject();
        networkTopology.add("peers", peersArray);
        networkTopology.add("connections", connectionsArray);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(pathToInputData))) {
            new GsonBuilder().setPrettyPrinting().create().toJson(networkTopology, writer);
            System.out.println("Network topology JSON has been saved to: " + pathToInputData);
        } catch (IOException e) {
            System.err.println("Error while writing the JSON file: " + e.getMessage());
        }
    }
}
