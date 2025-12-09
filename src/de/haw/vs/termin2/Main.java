package de.haw.vs.termin2;


import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        int DEFAULT_PORT = 3000;

        Server server = new Server();
        try {
            server.start(DEFAULT_PORT);
            server.pool().add("localhost", DEFAULT_PORT);
        } catch (IOException e) {
            System.err.println("Failed to initialize server");
            return;
        }

        System.out.println("Waiting 5 seconds for remote clients to connect...");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException _) {
        }

        Client client = new Client(server.pool());

        client.run();

        System.out.println("Running algorithm for 5 seconds");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException _) {
        }

        client.stop();
    }
}
