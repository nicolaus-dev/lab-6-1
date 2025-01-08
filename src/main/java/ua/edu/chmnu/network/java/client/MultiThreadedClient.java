package ua.edu.chmnu.network.java.client;

import java.io.*;
import java.net.*;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadedClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 9555;
    private static final int CLIENT_COUNT = 1000;

    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(50);

        for (int i = 0; i < CLIENT_COUNT; i++) {
            threadPool.submit(() -> runClientSession());
        }

        threadPool.shutdown();
    }

    private static void runClientSession() {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true)) {

            String randomString = UUID.randomUUID().toString();
            long startTime = System.nanoTime();

            output.println(randomString);
            String serverResponse = input.readLine();
            long endTime = System.nanoTime();

            long duration = (endTime - startTime) / 1_000_000; // Convert nanoseconds to milliseconds
            System.out.println("Client received: " + serverResponse + " | Total Duration: " + duration + " ms");

        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
        }
    }
}
