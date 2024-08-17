package ar.edu.itba.ss.g2.model;

import ar.edu.itba.ss.g2.Particle;

import java.util.List;
import java.util.Set;

public record Output(
        int L, int seed, double v, double noise, double rc, List<Set<Particle>> snapshots) {}
