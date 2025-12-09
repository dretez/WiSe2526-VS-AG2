package de.haw.vs.termin2;

import de.haw.vs.termin2.json.JSONReader;
import de.haw.vs.termin2.network.CommunicationInterface;
import de.haw.vs.termin2.network.Pool;
import de.haw.vs.termin2.process.LocalProcess;
import de.haw.vs.termin2.process.Process;
import de.haw.vs.termin2.process.RemoteProcess;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server {
    private static final int DEFAULT_PORT = 3000;
    private ServerSocket serverSocket;
    private final Pool pool;

    public Server() {
        this.pool = new Pool();
    }

    public Pool pool() {
        return this.pool;
    }

    public void start(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        while (true)
            new ClientHandler(serverSocket.accept(), pool).start();
    }

    public void stop() throws IOException {
        this.serverSocket.close();
        this.pool.close();
    }

    public static void main(String[] args) {
        Server server = new Server();
        try {
            server.start(DEFAULT_PORT);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return;
        }
        try {
            server.stop();
        } catch (IOException _) {
        }
    }

    private static class ClientHandler extends Thread {
        private final Socket clientSocket;
        private final Pool pool;
        private HashMap<Integer, Process> processes;

        public ClientHandler(Socket client, Pool pool) {
            this.clientSocket = client;
            this.pool = pool;
            try {
                this.pool.add(this.clientSocket);
            } catch (IOException _) {
            }
        }

        @Override
        public void run() {
            this.processes = new HashMap<>();
            try {
                while (true) {
                    String call = CommunicationInterface.awaitReply(this.clientSocket);
                    if (call == null) break;
                    handle(new JSONReader(call));
                }
            } catch (IOException _) {
            } finally {
                try {
                    this.clientSocket.close();
                    this.pool.remove(this.clientSocket);
                } catch (IOException _) {}
            }
        }

        private void handle(JSONReader json) {
            switch ((String) json.get("type")) {
                case "createProcess" -> createProcess(json);
                case "algorithmCall" -> handleAlgorithm(json);
                case "endAlgorithm" -> handleEndAlgorithm(json);
                default -> {
                    if (json.get("type") == null)
                        throw new IllegalArgumentException("Message doesn't specify type");
                    else
                        throw new IllegalArgumentException("Unexpected message type: " + json.get("type"));
                }
            }
        }

        private void createProcess(JSONReader json) {
            int number = (Integer) json.get("number");
            int id = (Integer) json.get("id");
            LocalProcess local = new LocalProcess(number, id);
            local.setPredecessor(new RemoteProcess(this.clientSocket, 0, (Integer) json.get("predecessor")));
            local.setSuccessor(new RemoteProcess(this.clientSocket, 0, (Integer) json.get("successor")));
            this.processes.put(id, local);
        }

        private void handleAlgorithm(JSONReader json) {
            int id = (Integer) json.get("id");
            int y = (Integer) json.get("y");
            processes.get(id).algorithm(y);
        }

        private void handleEndAlgorithm(JSONReader json) {
            for (var i : processes.entrySet()) {
                i.getValue().stop();
            }
        }
    }
}
