package de.haw.vs.termin2.process;

public class LocalProcess extends Process {
    public LocalProcess(int value) {
        super(value);
    }

    public LocalProcess(int value, int id) {
        super(value, id);
    }

    @Override
    public void algorithm(int y) {
        if (isStop()) return;
        if (y < divisor()) {
            setDivisor((divisor() - 1) % y + 1);
            new AlgorithmRequest(predecessor(), divisor()).start();
            new AlgorithmRequest(successor(), divisor()).start();
        }
    }
}
