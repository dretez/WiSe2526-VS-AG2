package de.haw.vs.termin2.network;

import de.haw.vs.termin2.json.JSONBuilder;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Pool extends Thread {
    private final ServerSocket serverSocket;
    private final List<Socket> pool;

    public Pool(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.pool = new ArrayList<>();
    }

    public int size() {
        return pool.size();
    }

    public ServerSocket serverSocket() {
        return serverSocket;
    }

    public List<Socket> pool() {
        return List.copyOf(pool);
    }

    public void add(Socket socket) throws IOException {
        if (this.pool.contains(socket)) return;
        String host = socket.getInetAddress().getHostAddress();
        int port = socket.getPort();
        JSONBuilder jb = new JSONBuilder();
        jb.putString("type", "newConnection");
        jb.putString("host", host);
        jb.putNumber("port", port);
        for (Socket connection : this.pool) {
            CommunicationInterface.sendRequest(connection, jb.toString());
        }
        this.pool.add(socket);
    }

    public void add(String host, int port) throws IOException {
        Socket socket = new Socket(host, port);
        this.add(socket);
    }

    public void close() throws IOException {
        this.serverSocket.close();
        for (Socket socket : pool) {
            socket.close();
        }
    }

    @Override
    public void run() {
        while (true) {
            try (Socket socket = this.serverSocket.accept()) {
                this.add(socket);
            } catch (IOException e) {
                break;
            }
        }
    }
}
