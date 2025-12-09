package de.haw.vs.termin2.process;

import de.haw.vs.termin2.json.JSONBuilder;
import de.haw.vs.termin2.network.CommunicationInterface;

import java.net.Socket;

public class RemoteProcess extends Process {
    Socket socket;

    public RemoteProcess(Socket socket, int value) {
        super(value);
        this.socket = socket;
        JSONBuilder jb = new JSONBuilder();
        jb.putString("type", "createProcess");
        jb.putNumber("number", value);
        jb.putNumber("id", this.id());
        try {
            CommunicationInterface.sendRequest(this.socket, jb.toString());
        } catch (Exception e) {
            System.err.println("Couldn't send request to remote process");
        }
    }

    @Override
    public void algorithm(int y) {
        JSONBuilder jb = new JSONBuilder();
        jb.putString("type", "algorithmCall");
        jb.putNumber("y", y);
        jb.putNumber("id", this.id());
        try {
            CommunicationInterface.sendRequest(this.socket, jb.toString());
        } catch (Exception e) {
            System.err.println("Couldn't send request to remote process");
        }
    }
}
