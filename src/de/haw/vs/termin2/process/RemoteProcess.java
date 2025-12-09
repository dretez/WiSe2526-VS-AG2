package de.haw.vs.termin2.process;

import de.haw.vs.termin2.json.JSONBuilder;
import de.haw.vs.termin2.json.JSONReader;
import de.haw.vs.termin2.network.CommunicationInterface;

import java.net.Socket;

public class RemoteProcess extends Process {
    Socket socket;

    public RemoteProcess(Socket socket, int value) {
        super(value);
        this.socket = socket;
    }

    public RemoteProcess(Socket socket, int value, int id) {
        super(value, id);
        this.socket = socket;
    }

    public void sendProcess() {
        JSONBuilder jb = new JSONBuilder();
        jb.putString("type", "createProcess");
        jb.putNumber("number", number());
        jb.putNumber("id", this.id());
        jb.putNumber("predecessor", predecessor().id());
        jb.putNumber("successor", successor().id());
        try {
            CommunicationInterface.sendRequest(this.socket, jb.toString());
        } catch (Exception e) {
            System.err.println("Couldn't send request to remote process");
        }
    }

    @Override
    public int divisor() {
        JSONBuilder jb = new JSONBuilder();
        jb.putString("type", "getDivisor");
        jb.putNumber("id", this.id());
        try {
            CommunicationInterface.sendRequest(this.socket, jb.toString());
            String json = CommunicationInterface.awaitReply(this.socket);
            return (int) new JSONReader(json).get("divisor");
        } catch (Exception e) {
            System.err.println("Couldn't send request to remote process");
        }
        return -1;
    }

    @Override
    public void algorithm(int y) {
        if (isStop()) return;
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
