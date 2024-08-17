package ar.edu.itba.ss.g2.simulation;

import ar.edu.itba.ss.g2.model.Particle;

import java.util.*;
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

        // M + 1 filas, para duplicar la ultima en la primera
        board = new ArrayList<>(M+1);

        // lleno con celdas
        for (int i = 0; i < M + 1; i++) {
            board.add(new ArrayList<>( M + 1));
            for (int j = 0; j < M + 1; j++) {
                board.get(i).add(new Cell());
            }
        }

        this.particles = new HashSet<>();

        for (Particle p : particles) {
            this.particles.add(p);
            int x = (int) ((p.getX() * M) / L);
            int y = (int) ((p.getY() * M) / L);
            board.get(x).get(y).addParticle(p);

            // Si y = 0, agrego la particula a la columna
            if (y == 0) {
                board.get(x).get(M).addParticle(p);
            }
        }

        // Copio la primera fila en la ultima
        board.set(M + 1, board.get(0));
    }

    private void checkAdjacent(List<List<Cell>> board, int x, int y, Particle p1, Map<Particle, Set<Particle>> neighbours) {
        if(x < 0 || y < 0 || x >= board.size() || y >= board.get(0).size()) {
            return;
        }
        Set<Particle> adjacentCellParticles = board.get(x).get(y).getParticles();
        for(Particle p2: adjacentCellParticles) {
            if(p1.toroidalDistanceTo(p2, L) < rc) {
                neighbours.get(p1).add(p2);
                neighbours.get(p2).add(p1);
            }
        }
    }

    private Map<Particle, Set<Particle>> getNeighbours() {
        Map<Particle, Set<Particle>> neighbours = new HashMap<>();
        // inicio todas las particulas
        particles.forEach(p -> neighbours.put(p, new HashSet<>()));

        for (int x = 0; x < board.size(); x++) {
            for (int y = 0; y < board.get(x).size(); y++) {
                // dada la siguiente submatriz
                // A B C
                // D E F
                // G H I
                // estoy parado en E.
                Set<Particle> currentCellParticles = board.get(x).get(y).getParticles();
                for (Particle p1 : currentCellParticles) {
                    // reviso E
                    checkAdjacent(board, x, y, p1, neighbours);
                    // me saco a mi mismo
                    neighbours.get(p1).remove(p1);

                    // reviso B
                    checkAdjacent(board, x - 1, y, p1, neighbours);
                    // reviso C
                    checkAdjacent(board, x - 1, y+1, p1, neighbours);
                    // reviso F
                    checkAdjacent(board, x, y+1, p1, neighbours);
                    // reviso I
                    checkAdjacent(board, x+1 , y+1, p1, neighbours);
                }
            }
        }
        return neighbours;
    }

    public void updateBoardState() {
        throw new IllegalStateException("Uninplemented");
    }

    public Set<Particle> getParticles() {
        return this.particles.stream().map(Particle::new).collect(Collectors.toSet());
    }
}
