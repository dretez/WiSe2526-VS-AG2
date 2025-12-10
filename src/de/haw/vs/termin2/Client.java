package de.haw.vs.termin2;

import de.haw.vs.termin2.network.Pool;
import de.haw.vs.termin2.process.LocalProcess;
import de.haw.vs.termin2.process.Process;
import de.haw.vs.termin2.process.RemoteProcess;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Client {
    private final Pool pool;
    private ArrayList<Process> procList;

    public Client(Pool pool) {
        this.pool = pool;
    }

    public void run() {
        ArrayList<Integer> numbers = new ArrayList<>(Arrays.asList(108, 120, 60, 36));
        List<Socket> currentPool = pool.pool();
        int remoteCount = currentPool.size();
        int procPerSock = Math.max(1, numbers.size() / (currentPool.size() + 1));

        System.out.println("Connected clients: " + remoteCount);

        procList = new ArrayList<>();

        for (Socket sock: currentPool) {
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
            if (proc instanceof RemoteProcess)
                ((RemoteProcess) proc).sendProcess();
        }

        procList.getLast().start();
    }

    public void stop() {
        for (var proc : procList) {
            proc.stop();
        }
        for (var proc : procList) {
            System.out.println("Max common divisor for " + proc.number() + ": " + proc.divisor());
        }
        pool.close();
    }
}
