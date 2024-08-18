package ar.edu.itba.ss.g2.simulation;

import ar.edu.itba.ss.g2.model.Particle;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Simulation {

    private final List<Set<Particle>> snapshots;

    private final BoardState boardState;

    private final long t;

    public Simulation(
            Set<Particle> particles, int L, double v, double noise, double rc, long t, int seed) {

        this.boardState = new BoardState(L, v, noise, rc, particles, seed);

        this.t = t;

        this.snapshots = new ArrayList<>((int) t);
    }

    public void run() {

        snapshots.add(boardState.getParticles());

        for (int i = 1; i < t; i++) {
            boardState.updateBoardState();
            snapshots.add(boardState.getParticles());
        }
    }

    public List<Set<Particle>> getSnapshots() {
        return snapshots;
    }
}
