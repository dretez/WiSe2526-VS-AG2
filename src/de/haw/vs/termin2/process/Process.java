package de.haw.vs.termin2.process;

import de.haw.vs.termin2.AlgorithmRequest;

public abstract class Process {
    private final int number;
    private int divisor;
    private Process predecessor, successor;

    public Process(int value) {
        this.number = value;
        this.divisor = value;
    }

    public int getNumber() {
        return number;
    }

    public int getDivisor() {
        return divisor;
    }

    public void setPredecessor(Process predecessor) {
        this.predecessor = predecessor;
    }

    public void setSuccessor(Process successor) {
        this.successor = successor;
    }

    public void algorithm(int y) {
        if (y < divisor) {
            divisor = (divisor - 1) % y + 1;
            new AlgorithmRequest(predecessor, divisor).start();
            new AlgorithmRequest(successor, divisor).start();
        }
    }
}
