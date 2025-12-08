package de.haw.vs.termin2.process;

public abstract class Process {
    private final int number;
    private int divisor;
    private final int id;
    private Process predecessor, successor;
    private static int nextId = 0;

    public Process(int value) {
        this.number = value;
        this.divisor = value;
        this.id = ++nextId;
    }

    public int id() {
        return id;
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

    static class AlgorithmRequest extends Thread {
        private final Process process;
        private final int num;

        public AlgorithmRequest(Process process, int num) {
            this.process = process;
            this.num = num;
        }

        @Override
        public void run() {
            this.process.algorithm(num);
        }
    }
}
