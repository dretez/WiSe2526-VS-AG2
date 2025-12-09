package de.haw.vs.termin2.process;

import de.haw.vs.termin2.json.JSONBuilder;
import de.haw.vs.termin2.network.CommunicationInterface;

import java.io.IOException;
import java.net.Socket;

public class ServerProcess extends Process {
    private final Socket socket;

    public ServerProcess(Socket socket, int value, int id) {
        super(value, id);
        this.socket = socket;
    }

    @Override
    public void algorithm(int y) {
        if (isStop()) return;
        if (y < divisor()) {
            setDivisor((divisor() - 1) % y + 1);
            JSONBuilder jb = new JSONBuilder();
            jb.putString("type", "returnDivisor");
            jb.putNumber("divisor", divisor());
            try {
                CommunicationInterface.sendRequest(socket, jb.toString());
            } catch (IOException _) {
            }
        }
    }
}
