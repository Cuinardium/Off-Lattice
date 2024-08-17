package ar.edu.itba.ss.g2.simulation;

import ar.edu.itba.ss.g2.model.Particle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BoardState {
    private final List<List<Cell>> board;
    private final Set<Particle> particles;

    private final int L;

    private final double v;
    private final double noise;
    private final double rc; 

    public BoardState(int L, double v, double noise, double rc, Set<Particle> particles) {
        this.L = L;

        this.v = v;
        this.noise = noise;
        this.rc = rc;

        if(L <= 0) {
            throw new IllegalArgumentException("L should be > 0");
        }
        int M = (int) Math.floor(L/rc);

        if(M <= 0) {
            M = 1;
        }

        board = new ArrayList<>(M+2);

        // Agrego una columna extra al principio y una mas al final
        for (int i = 0; i < M + 2; i++) {
            board.add(new ArrayList<>((int) M + 1));

            // Agrego una celda extra por cada columna
            for (int j = 0; j < M + 1; j++) {
                board.get(i).add(new Cell());
            }
        }

        this.particles = new HashSet<>();

        for (Particle p : particles) {
            this.particles.add(p);
            int x = (int) ((p.getX() * M) / L);
            int y = (int) ((p.getY() * M) / L);
            board.get(x + 1).get(y).addParticle(p);

            // Si y = 0, agrego la particula a la celda extra
            if (y == 0) {
                board.get(x + 1).get((int) M).addParticle(p);
            }
        }

        // Copio la primera columna en la ultima y la ultima en la primera
        board.set((int) M + 1, board.get(1));
        board.set(0, board.get((int) M));
    }

    public void updateBoardState() {
        throw new IllegalStateException("Uninplemented");
    }

    public Set<Particle> getParticles() {
        return this.particles.stream().map(p -> new Particle(p)).collect(Collectors.toSet());
    }
}
