package ar.edu.itba.ss.g2;

import java.util.*;

public class CellIndexMethod {

    public static Map<Particle, Set<Particle>> calculate(
            List<Particle> particles, Long L, Long M, Double rc, boolean isToroidal) {
        List<List<Set<Particle>>> grid =
                isToroidal ? generateToroidalGrid(particles, L, M) : generateGrid(particles, L, M);

        Map<Particle, Set<Particle>> map = new HashMap<>();

        for (int i = 0; i < M; i++) {
            for (int j = 0; j < M; j++) {

                // Si es toroidal, la primer columna es extra
                int x = isToroidal ? i + 1 : i;
                int y = j;

                for (Particle p : grid.get(x).get(y)) {
                    checkAdjacent(grid, x, y, rc, p, map, isToroidal, L);
                }
            }
        }

        return map;
    }

    private static void checkAdjacent(
            List<List<Set<Particle>>> grid,
            int x,
            int y,
            double rc,
            Particle p,
            Map<Particle, Set<Particle>> map,
            boolean isToroidal,
            long L) {
        if (x < 0 || y < 0 || x >= grid.size() || y >= grid.get(x).size()) {
            return;
        }

        // tengo
        // - - -
        // - x -
        // - - -

        // reviso
        // - x x
        // - x x
        // - - -
        for (int i = Math.max(x - 1, 0); i <= x; i++) {
            for (int j = y; j <= Math.min(y + 1, grid.size() - 1); j++) {
                for (Particle p2 : grid.get(i).get(j)) {
                    if (!p.equals(p2)) {
                        addIfClose(p, p2, rc, map, isToroidal, L);
                    }
                }
            }
        }
        // reviso
        // - - -
        // - - -
        // - - x
        x = x + 1;
        y = y + 1;
        if (!(x >= grid.size() || y >= grid.get(x).size())) {
            for (Particle p2 : grid.get(x).get(y)) {
                // Puede pasar que esta celda sea la de x, y
                if (!isToroidal || !p.equals(p2)) {
                    addIfClose(p, p2, rc, map, isToroidal, L);
                }
            }
        }
    }

    private static List<List<Set<Particle>>> generateGrid(
            List<Particle> particles, long L, long M) {
        List<List<Set<Particle>>> grid = new ArrayList<>();

        for (int i = 0; i < M; i++) {
            grid.add(new ArrayList<>());
            for (int j = 0; j < M; j++) {
                grid.get(i).add(new HashSet<>());
            }
        }

        for (Particle p : particles) {
            int x = (int) ((p.getX() * M) / L);
            int y = (int) ((p.getY() * M) / L);
            grid.get(x).get(y).add(p);
        }

        return grid;
    }

    private static List<List<Set<Particle>>> generateToroidalGrid(
            List<Particle> particles, long L, long M) {
        List<List<Set<Particle>>> grid = new ArrayList<>((int) M + 2);

        // Agrego una columna extra al principio y una mas al final
        for (int i = 0; i < M + 2; i++) {
            grid.add(new ArrayList<>((int) M + 1));

            // Agrego una celda extra por cada columna
            for (int j = 0; j < M + 1; j++) {
                grid.get(i).add(new HashSet<>());
            }
        }

        for (Particle p : particles) {
            int x = (int) ((p.getX() * M) / L);
            int y = (int) ((p.getY() * M) / L);
            grid.get(x + 1).get(y).add(p);

            // Si y = 0, agrego la particula a la celda extra
            if (y == 0) {
                grid.get(x + 1).get((int) M).add(p);
            }
        }

        // Copio la primera columna en la ultima y la ultima en la primera
        grid.set((int) M + 1, grid.get(1));
        grid.set(0, grid.get((int) M));

        return grid;
    }

    private static void addIfClose(
            Particle p1,
            Particle p2,
            Double rc,
            Map<Particle, Set<Particle>> map,
            boolean isToroidal,
            long L) {

        double distance = isToroidal ? p1.toroidalDistanceTo(p2, L) : p1.distanceTo(p2);

        if (distance < rc) {
            if (!map.containsKey(p1)) {
                map.put(p1, new HashSet<>());
            }
            if (!map.containsKey(p2)) {
                map.put(p2, new HashSet<>());
            }
            map.get(p1).add(p2);
            map.get(p2).add(p1);
        }
    }
}
