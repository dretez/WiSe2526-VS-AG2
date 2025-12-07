package de.haw.vs.termin2.network;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class CommunicationInterface {
    private CommunicationInterface() {}

    public static void sendRequest(Socket socket, String json) throws IOException {
        json = json.replace("\n", " ").replace("\r", " ");
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
        out.write(json);
        out.write("\n");
        out.flush();
    }

    public static String awaitReply(Socket socket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        return in.readLine();
    }
}
