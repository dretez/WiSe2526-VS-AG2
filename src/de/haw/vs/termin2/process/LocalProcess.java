package de.haw.vs.termin2.process;

public class LocalProcess extends Process {
    public LocalProcess(int value) {
        super(value);
    }

    @Override
    public void algorithm(int y) {
        if (y < divisor()) {
            setDivisor((divisor() - 1) % y + 1);
            new AlgorithmRequest(predecessor(), divisor()).start();
            new AlgorithmRequest(successor(), divisor()).start();
        }
    }

    private static class AlgorithmRequest extends Thread {
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
