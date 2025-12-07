package de.haw.vs.termin2;


import de.haw.vs.termin2.network.Pool;
import de.haw.vs.termin2.process.LocalProcess;
import de.haw.vs.termin2.process.Process;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        int DEFAULT_PORT = 3000;

        Pool pool;
        try {
            pool = new Pool(DEFAULT_PORT);
        } catch (IOException e) {
            System.err.println("Failed to start pool");
            return;
        }

        /* Connect to all other servers (create a pool) */
        // pool.add("localhost", DEFAULT_PORT);

        /* Initialize numbers */
        List<Integer> numbers = Arrays.asList(108, 7, 60, 36);

        /* Try to evenly distribute numbers by all servers in the pool.
         * Keep any remaining numbers */

        /* Setup timeout */

        /* Start algorithm */

        /*
        Process[] ring = {new LocalProcess(84), new LocalProcess(60), new LocalProcess(36)};
        for (int i = 0; i < ring.length; i++) {
            ring[i].setPredecessor(ring[((i - 1) % ring.length + ring.length) % ring.length]);
            ring[i].setSuccessor(ring[(i + 1) % ring.length]);
        }
        ring[0].algorithm(ring[1].getNumber());
        System.out.println(ring[0].getNumber() + " " + ring[0].getDivisor());
        System.out.println(ring[1].getNumber() + " " + ring[1].getDivisor());
        System.out.println(ring[2].getNumber() + " " + ring[2].getDivisor());
         */
    }
}
