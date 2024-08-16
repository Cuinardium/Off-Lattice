package ar.edu.itba.ss.g2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParticleGenerator {

    private final int areaLength;
    private final int particleAmount;

    private final Random random;

    public ParticleGenerator(int areaLength, int particleAmount, int seed) {

        if (areaLength <= 0) {
            throw new IllegalArgumentException("Area length must be greater than 0");
        }

        if (particleAmount <= 0) {
            throw new IllegalArgumentException("Particle amount must be greater than 0");
        }

        this.areaLength = areaLength;
        this.particleAmount = particleAmount;
        this.random = new Random(seed);
    }

    public List<Particle> generate() {
        List<Particle> particles = new ArrayList<>(particleAmount);

        for (long i = 0; i < particleAmount; i++) {
            Double x = random.nextDouble() * areaLength;
            Double y = random.nextDouble() * areaLength;

            // (0, 1) * 2 pi
            Double direction = random.nextDouble() * 2 * Math.PI;

            particles.add(new Particle(i, x, y, direction));
        }

        return particles;
    }
}
