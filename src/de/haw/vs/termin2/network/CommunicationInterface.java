package de.haw.vs.termin2.network;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

public class CommunicationInterface {
    private CommunicationInterface() {}

    private static final Map<Socket, Object> socketLocks = Collections.synchronizedMap(new WeakHashMap<>());
    private static final Map<Socket, BufferedWriter> outputstreams = new HashMap<>();
    private static final Map<Socket, BufferedReader> inputstreams = new HashMap<>();

    private static Object getLock(Socket socket) {
        return socketLocks.computeIfAbsent(socket, s -> new Object());
    }

    private static BufferedWriter getWriter(Socket socket) {
        return outputstreams.computeIfAbsent(socket, s -> {
            try {
                return new BufferedWriter(new OutputStreamWriter(s.getOutputStream(), StandardCharsets.UTF_8));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static BufferedReader getReader(Socket socket) {
        return inputstreams.computeIfAbsent(socket, s -> {
            try {
                return new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void sendRequest(Socket socket, String json) throws IOException {
        json = json.replace("\n", " ").replace("\r", " ");
        BufferedWriter out = getWriter(socket);
        out.write(json);
        out.write("\n");
        out.flush();
    }

    public static String awaitReply(Socket socket) throws IOException {
        BufferedReader in = getReader(socket);
        return in.readLine();
    }

    public static String sendAndAwait(Socket socket, String json) throws IOException {
        synchronized (getLock(socket)) {
            sendRequest(socket, json);
            return awaitReply(socket);
        }
    }
}
