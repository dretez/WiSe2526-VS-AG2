package de.haw.vs.termin2;

import de.haw.vs.termin2.json.JSONBuilder;
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

public class Server implements Runnable {
    private static final int DEFAULT_PORT = 3000;
    private ServerSocket serverSocket;
    private final Pool pool;
    private int port;

    public Server(int port) {
        this.pool = new Pool();
        this.port = port;
    }

    public Pool pool() {
        return this.pool;
    }

    public void stop() throws IOException {
        this.serverSocket.close();
        this.pool.close();
    }

    public static void main(String[] args) {
        Server server = new Server(DEFAULT_PORT);
        new Thread(server).start();
    }

    @Override
    public void run() {
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return;
        }
        while (true) {
            try {
                new ClientHandler(serverSocket.accept(), pool).start();
            } catch (IOException e) {
                break;
            }
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
                case "getDivisor" -> handleGetDivisor(json);
                case "newConnection" -> handleNewConnection(json);
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

        private void handleGetDivisor(JSONReader json) {
            int id = (int) json.get("id");
            int divisor = processes.get(id).divisor();
            JSONBuilder jb = new JSONBuilder();
            jb.putString("type", "returnDivisor");
            jb.putNumber("divisor", divisor);
            try {
                CommunicationInterface.sendRequest(clientSocket, jb.toString());
            } catch (IOException _) {
            }
        }

        private void handleNewConnection(JSONReader json) {
            String host = (String) json.get("host");
            int port = (int) json.get("port");
            try {
                pool.add(host, port);
            } catch (IOException _) {
            }
        }
    }
}
