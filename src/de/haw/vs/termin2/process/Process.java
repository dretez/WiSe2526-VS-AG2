package de.haw.vs.termin2.process;

public abstract class Process {
    private final int number;
    private int divisor;
    private final int id;
    private Process predecessor, successor;
    private static int nextId = 0;
    private boolean stop;

    public abstract void algorithm(int y);

    public Process(int value, int id) {
        this.number = value;
        this.divisor = value;
        this.id = id;
        this.stop = false;
        this.predecessor = null;
        this.successor = null;
    }
    public Process(int value) {
        this(value, nextId++);
    }

    public boolean isStop() {
        return stop;
    }

    public int id() {
        return id;
    }

    public int number() {
        return number;
    }

    public int divisor() {
        return divisor;
    }

    protected Process predecessor() {
        return predecessor;
    }

    protected Process successor() {
        return successor;
    }

    public void stop() {
        stop = true;
    }

    protected void setDivisor(int value) {
        divisor = value;
    }

    public void setPredecessor(Process predecessor) {
        this.predecessor = predecessor;
    }

    public void setSuccessor(Process successor) {
        this.successor = successor;
    }

    @Override
    public String toString() {
        return this.number + ":" + this.divisor;
    }

    public void start() {
        new AlgorithmRequest(predecessor, number).start();
        new AlgorithmRequest(successor, number).start();
    }

    protected static class AlgorithmRequest extends Thread {
        private final Process process;
        private final int num;

        public AlgorithmRequest(Process process, int num) {
            this.process = process;
            this.num = num;
        }

        @Override
        public void run() {
            System.out.println("What?");
            this.process.algorithm(num);
        }
    }
}
