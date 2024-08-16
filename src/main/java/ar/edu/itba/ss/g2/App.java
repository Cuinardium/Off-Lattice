package ar.edu.itba.ss.g2;

import ar.edu.itba.ss.g2.args.ArgParser;
import ar.edu.itba.ss.g2.args.Configuration;

import java.util.List;

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

        particles.forEach(System.out::println);
    }
}
