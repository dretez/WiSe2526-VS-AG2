package de.haw.vs.termin2;


public class Main {
    public static void main(String[] args) {
        Process[] ring = {new Process(84), new Process(60), new Process(36)};
        for (int i = 0; i < ring.length; i++) {
            ring[i].setPredecessor(ring[((i - 1) % ring.length + ring.length) % ring.length]);
            ring[i].setSuccessor(ring[(i + 1) % ring.length]);
        }
        ring[0].algorithm(ring[1].getNumber());
        System.out.println(ring[0].getNumber() + " " + ring[0].getDivisor());
        System.out.println(ring[1].getNumber() + " " + ring[1].getDivisor());
        System.out.println(ring[2].getNumber() + " " + ring[2].getDivisor());
    }
}
