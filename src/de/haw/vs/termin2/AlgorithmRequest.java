package de.haw.vs.termin2;

import de.haw.vs.termin2.process.Process;

public class AlgorithmRequest extends Thread {
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
