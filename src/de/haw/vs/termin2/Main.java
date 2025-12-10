package de.haw.vs.termin2;


import de.haw.vs.termin2.network.Pool;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        int DEFAULT_PORT = 3000;

        Pool pool = new Pool();
        try {
            pool.add("100.74.192.93", DEFAULT_PORT);
        } catch (IOException e) {
            System.err.println("Failed to initialize server");
            return;
        }

        Client client = new Client(pool);

        client.run();

        System.out.println("Running algorithm for 5 seconds");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException _) {
        }

        System.out.println("Stopping");
        client.stop();
    }
}
