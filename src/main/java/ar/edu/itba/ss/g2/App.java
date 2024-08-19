package ar.edu.itba.ss.g2;

import ar.edu.itba.ss.g2.config.ArgParser;
import ar.edu.itba.ss.g2.config.Configuration;
import ar.edu.itba.ss.g2.model.Output;
import ar.edu.itba.ss.g2.model.Particle;
import ar.edu.itba.ss.g2.simulation.ParticleGenerator;
import ar.edu.itba.ss.g2.simulation.Simulation;
import ar.edu.itba.ss.g2.utils.FileUtil;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class App {
    public static void main(String[] args) {

        ArgParser parser = new ArgParser(args);
        Configuration configuration = parser.parse();

        if (configuration == null) {
            parser.printHelp();
            System.exit(1);
        }

        int L = configuration.getL();
        int N = configuration.getN();
        int seed = configuration.getSeed();

        List<Particle> particles = new ParticleGenerator(L, N, seed).generate();

        double v = configuration.getV();
        double noise = configuration.getNoise();
        double rc = configuration.getRc();
        int t = configuration.getT();

        Simulation simulation = new Simulation(Set.copyOf(particles), L, v, noise, rc, t, seed);

        simulation.run();

        List<Set<Particle>> snapshots = simulation.getSnapshots();

        String outputDirectory = configuration.getOutputDirectory();

        try {
            Output output = new Output(L, seed, v, noise, rc, snapshots);
            FileUtil.serializeOutput(output, outputDirectory);
        } catch (IOException e) {
            System.err.println("Error writing output file: " + e.getMessage());
            System.exit(1);
        }
    }
}
