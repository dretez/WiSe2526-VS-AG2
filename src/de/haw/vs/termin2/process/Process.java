package de.haw.vs.termin2.process;

public abstract class Process {
    private final int number;
    private int divisor;
    private final int id;
    private Process predecessor, successor;
    private static int nextId = 0;

    public abstract void algorithm(int y);

    public Process(int value) {
        this.number = value;
        this.divisor = value;
        this.id = ++nextId;
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

    protected void setDivisor(int value) {
        divisor = value;
    }

    public void setPredecessor(Process predecessor) {
        this.predecessor = predecessor;
    }

    public void setSuccessor(Process successor) {
        this.successor = successor;
    }
}
