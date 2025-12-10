package de.haw.vs.termin2.process;


public class ServerProcess extends Process {

    public ServerProcess(int value, int id) {
        super(value, id);
    }

    @Override
    public void algorithm(int y) {
        if (isStop()) return;
        if (y < divisor()) {
            setDivisor((divisor() - 1) % y + 1);
        }
    }
}
