package de.haw.vs.termin2;


import de.haw.vs.termin2.network.CommunicationInterface;
import de.haw.vs.termin2.network.Pool;
import de.haw.vs.termin2.process.LocalProcess;
import de.haw.vs.termin2.process.Process;
import de.haw.vs.termin2.process.RemoteProcess;


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
            pool.start();
        } catch (IOException e) {
            System.err.println("Failed to start pool");
            return;
        }
        /* Initialize numbers */
        List<Integer> numbers = Arrays.asList(108, 7, 60, 36);
        /* Try to evenly distribute numbers by all servers in the pool.
         * Keep any remaining numbers */
        System.out.println("Waiting 5 seconds for remote clients to connect...");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        List<Socket> sockets = pool.pool();
        int remoteCount = sockets.size();

        System.out.println("Connected clients: " + remoteCount);


        Process[] processes = new Process[numbers.size()];

        for (int i = 0; i < numbers.size(); i++) {

            if (remoteCount == 0) {
                processes[i] = new LocalProcess(numbers.get(i));
                continue;
            }

            Socket assignedSocket = sockets.get(i % remoteCount);
            processes[i] = new RemoteProcess(assignedSocket, numbers.get(i));
        }


    }
}
