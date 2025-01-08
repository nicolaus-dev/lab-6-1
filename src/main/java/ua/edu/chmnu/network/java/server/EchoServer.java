package ua.edu.chmnu.network.java.server;

import java.io.*;
import java.net.*;
import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EchoServer {
    private static final int PORT = 9555;
    private static final int THREAD_POOL_SIZE = 100;

    public static void main(String[] args) {
        System.out.println("Server is running on port " + PORT + "...");
        ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                threadPool.submit(() -> handleClient(clientSocket));
            }
        } catch (IOException e) {
            System.err.println("Error in server: " + e.getMessage());
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String clientMessage;
            while ((clientMessage = input.readLine()) != null) {
                long startTime = System.nanoTime();
                String serverResponse = "Processed: " + clientMessage + " | Timestamp: " + Instant.now();
                long endTime = System.nanoTime();
                long duration = (endTime - startTime) / 1_000_000; // Convert nanoseconds to milliseconds

                output.println(serverResponse + " | Processing Duration: " + duration + " ms");
            }
        } catch (IOException e) {
            System.err.println("Client connection error: " + e.getMessage());
        }
    }
}
