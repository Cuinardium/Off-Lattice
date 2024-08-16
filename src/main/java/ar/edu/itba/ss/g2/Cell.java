package ar.edu.itba.ss.g2;

import java.util.HashSet;
import java.util.Set;

public class Cell {
    private boolean revisionStatus = false;
    private final Set<Particle> particles = new HashSet<>();

    public void addParticle(Particle particle) {
        particles.add(particle);
    }

    public Set<Particle> getParticles() {
        return particles;
    }

    public void setRevisionStatus(boolean revisionStatus) {
        this.revisionStatus = revisionStatus;
    }

    public boolean getRevisionStatus() {
        return revisionStatus;
    }
}
