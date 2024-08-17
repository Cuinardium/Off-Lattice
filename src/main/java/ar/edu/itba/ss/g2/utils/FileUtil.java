package ar.edu.itba.ss.g2.utils;

import ar.edu.itba.ss.g2.model.Output;
import ar.edu.itba.ss.g2.Particle;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class FileUtil {

    private FileUtil() {
        throw new RuntimeException("Util class");
    }

    public static void serializeOutput(Output output, String directory)
            throws IOException {

        // Create directory if it doesn't exist
        File dir = new File(directory);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Dynamic
        try (FileWriter writer = new FileWriter(directory + "/dynamic.txt")) {

            long t = 0;
            for (Set<Particle> snapshot : output.snapshots()) {

                writer.write(t + "\n");

                List<Particle> sortedParticles =
                        snapshot.stream()
                                .sorted(Comparator.comparingLong(Particle::getId))
                                .toList();

                for (Particle particle : sortedParticles) {
                    writer.write(
                            particle.getX()
                                    + " "
                                    + particle.getY()
                                    + " "
                                    + particle.getDirection()
                                    + "\n");
                }

                t++;
            }
        }

        // Static
        try (FileWriter writer = new FileWriter(directory + "/static.txt")) {
            writer.write(output.L() + "\n");
            writer.write(output.seed() + "\n");
            writer.write(output.v() + "\n");
            writer.write(output.noise() + "\n");
            writer.write(output.rc() + "\n");
        }
    }
}
