package ar.edu.itba.ss.g2.simulation;

import ar.edu.itba.ss.g2.model.Particle;

import java.util.*;
import java.util.stream.Collectors;

public class BoardState {
    private final List<List<Set<Particle>>> board;
    private final Set<Particle> particles;

    private final int L;
    private final int M;

    private final double v;
    private final double noise;
    private final double rc; 
    
    private final Random random;

    public BoardState(int L, double v, double noise, double rc, Set<Particle> particles, int seed) {
        this.L = L;

        this.v = v;
        this.noise = noise;
        this.rc = rc;

        this.random = new Random(seed);

        if(L <= 0) {
            throw new IllegalArgumentException("L should be > 0");
        }
        int M = (int) Math.floor(L/rc);

        if(M <= 0) {
            M = 1;
        }

        this.M = M;

        // M + 1 filas, para duplicar la ultima en la primera
        board = new ArrayList<>(M+1);

        // lleno con celdas
        for (int i = 0; i < M; i++) {
            board.add(new ArrayList<>( M + 1));
            for (int j = 0; j < M; j++) {
                board.get(i).add(new HashSet<>());
            }

            // Agrego la misma celda de la columna 0
            // en la columna M
            board.get(i).add(board.get(i).get(0));
        }

        // Agrego la misma fila de la fila 0
        // en la fila M
        board.add(board.get(0));

        this.particles = new HashSet<>();

        for (Particle p : particles) {
            this.particles.add(p);
            int x = (int) ((p.getX() * M) / L);
            int y = (int) ((p.getY() * M) / L);
            board.get(x).get(y).add(p);
        }
    }

    private void checkAdjacent(List<List<Set<Particle>>> board, int x, int y, Particle p1, Map<Particle, Set<Particle>> neighbours) {
        if(x < 0 || y < 0 || x >= board.size() || y >= board.get(0).size()) {
            return;
        }
        Set<Particle> adjacentCellParticles = board.get(x).get(y);
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
                Set<Particle> currentCellParticles = board.get(x).get(y);
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
        Map<Particle, Set<Particle>> neighbours = getNeighbours();

        for (Particle p : particles) {

            double x = p.getX();
            double y = p.getY();

            updateParticle(p, neighbours.get(p));

            // Si se fue de la celda, la saco y la agrego a la nueva
            int xOld = (int) ((x * M) / L);
            int yOld = (int) ((y * M) / L);

            int xNew = (int) ((p.getX() * M) / L);
            int yNew = (int) ((p.getY() * M) / L);

            if (xOld != xNew || yOld != yNew) {
                board.get(xOld).get(yOld).remove(p);
                board.get(xNew).get(yNew).add(p);
            }
        }
    }

    public Set<Particle> getParticles() {
        return this.particles.stream().map(Particle::new).collect(Collectors.toSet());
    }


    // TODO: A lo mejor mover a la clase particle?
    private void updateParticle(Particle p, Set<Particle> neighbours) {

        double x = p.getX();
        double y = p.getY();
        double direction = p.getDirection();

        double dx = v * Math.cos(direction);
        double dy = v * Math.sin(direction);


        double sinSum = Math.sin(direction);
        double cosSum = Math.cos(direction);

        for (Particle particle : neighbours) {
            sinSum += Math.sin(particle.getDirection());
            cosSum += Math.cos(particle.getDirection());
        }
        
        double sinAvg = sinSum / (neighbours.size() + 1);
        double cosAvg = cosSum / (neighbours.size() + 1);

        // TODO: Porque son 2 parametros?
        // En clase dice atan2(sinAvg / cosAvg)
        double directionAvg = Math.atan2(sinAvg, cosAvg);

        // directionNoise : [-noise/2, noise/2]
        double directionNoise = (random.nextDouble() - 0.5) * noise;

        double newX = (x + dx) % L;
        if (newX < 0) {
            newX += L;
        }

        double newY = (y + dy) % L;
        if (newY < 0) {
            newY += L;
        }

        // TODO: Creo que no es necesario hacer el modulo porque
        // Cos y Sin son periodicas
        double newDirection = (directionAvg + directionNoise) % (2 * Math.PI);
        if (newDirection < 0) {
            newDirection += 2 * Math.PI;
        }

        p.setX(newX);
        p.setY(newY);
        p.setDirection(newDirection);
    }
}
