package ar.edu.itba.ss.g2.simulation;

import ar.edu.itba.ss.g2.model.Particle;

import java.util.HashSet;
import java.util.Set;

public class Cell {
    private final Set<Particle> particles = new HashSet<>();

    public void addParticle(Particle particle) {
        particles.add(particle);
    }

    public Set<Particle> getParticles() {
        return particles;
    }
}
