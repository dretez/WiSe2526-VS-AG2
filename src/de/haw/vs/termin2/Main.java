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
        int procPerSock = Math.max(1, (sockets.size() + 1) / numbers.size());

        System.out.println("Connected clients: " + remoteCount);

        ArrayList<Process> procList = new ArrayList<>();

        for (Socket sock: sockets) {
            for (int i = 0; i < procPerSock && numbers.size() > 1; i++) {
                procList.add(new RemoteProcess(sock, numbers.removeFirst()));
            }
            if (numbers.size() == 1) break;
        }
        for (var num : numbers) {
            procList.add(new LocalProcess(num));
        }
        for (int i = 0; i < procList.size(); i++) {
            Process proc = procList.get(i);
            proc.setPredecessor(i == 0 ? procList.getLast() : procList.get(i-1));
            proc.setSuccessor(i+1 == procList.size() ? procList.getFirst() : procList.get(i+1));
        }
    }
}
