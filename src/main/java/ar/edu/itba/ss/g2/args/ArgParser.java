package ar.edu.itba.ss.g2.args;

import org.apache.commons.cli.*;

import java.util.Comparator;
import java.util.List;

public class ArgParser {

    private static final List<Option> OPTIONS =
            List.of(
                    new Option("h", "help", false, "Show help"),
                    new Option("out", "output", true, "Output directory"),

                    // For particle generation
                    new Option("s", "seed", true, "Seed for random number generator"),
                    new Option("N", "number", true, "Amount of particles to be generated"),
                    new Option("L", "length", true, "Length of the area to be analyzed"),

                    // For simulation
                    new Option("v", "velocity", true, "Velocity of the particles"),
                    new Option("n", "noise", true, "Noise in the velocity of the particles"),
                    new Option("rc", "interaction-radius", true, "Interaction radius"),
                    new Option("t", "time", true, "Amount of steps (t) to simulate"));

    private final String[] args;
    private final Options options;

    public ArgParser(String[] args) {
        this.args = args;

        Options options = new Options();
        OPTIONS.forEach(options::addOption);
        this.options = options;
    }

    public Configuration parse() {

        CommandLineParser parser = new DefaultParser();

        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.err.println("Error parsing arguments: " + e.getMessage());
            return null;
        }

        if (cmd.hasOption("h")) {
            return null;
        }

        Configuration.Builder builder = new Configuration.Builder();

        // Particle generation parameters

        if (cmd.hasOption("N")) {
            int N;
            try {
                N = Integer.parseInt(cmd.getOptionValue("N"));
            } catch (NumberFormatException e) {
                System.err.println("Invalid number of particles: " + cmd.getOptionValue("N"));
                return null;
            }

            if (N <= 0) {
                System.err.println("Number of particles must be greater than 0");
                return null;
            }

            builder.N(N);
        } else {
            System.err.println("Number of particles is required");
            return null;
        }

        if (cmd.hasOption("L")) {
            int L;
            try {
                L = Integer.parseInt(cmd.getOptionValue("L"));
            } catch (NumberFormatException e) {
                System.err.println("Invalid length: " + cmd.getOptionValue("L"));
                return null;
            }

            if (L <= 0) {
                System.err.println("Length must be greater than 0");
                return null;
            }

            builder.L(L);

        } else {
            System.err.println("Length is required");
            return null;
        }

        if (cmd.hasOption("s")) {
            int seed;
            try {
                seed = Integer.parseInt(cmd.getOptionValue("s"));
            } catch (NumberFormatException e) {
                System.err.println("Invalid seed: " + cmd.getOptionValue("s"));
                return null;
            }

            builder.seed(seed);
        }

        // Simulation parameters

        if (cmd.hasOption("v")) {
            double v;
            try {
                v = Double.parseDouble(cmd.getOptionValue("v"));
            } catch (NumberFormatException e) {
                System.err.println("Invalid velocity: " + cmd.getOptionValue("v"));
                return null;
            }

            if (v <= 0) {
                System.err.println("Velocity must be greater than 0");
                return null;
            }

            builder.v(v);
        } else {
            System.err.println("Velocity is required");
            return null;
        }

        if (cmd.hasOption("n")) {
            double noise;
            try {
                noise = Double.parseDouble(cmd.getOptionValue("n"));
            } catch (NumberFormatException e) {
                System.err.println("Invalid noise: " + cmd.getOptionValue("n"));
                return null;
            }

            if (noise < 0) {
                System.err.println("Noise must be greater than or equal to 0");
                return null;
            }

            builder.noise(noise);
        } else {
            System.err.println("Noise is required");
            return null;
        }

        if (cmd.hasOption("rc")) {
            double rc;
            try {
                rc = Double.parseDouble(cmd.getOptionValue("rc"));
            } catch (NumberFormatException e) {
                System.err.println("Invalid cutoff radius: " + cmd.getOptionValue("rc"));
                return null;
            }

            if (rc <= 0) {
                System.err.println("Cutoff radius must be greater than 0");
                return null;
            }

            builder.rc(rc);
        } else {
            System.err.println("Cutoff radius is required");
            return null;
        }

        if (cmd.hasOption("out")) {
            builder.outputDirectory(cmd.getOptionValue("out"));
        } else {
            System.err.println("Output directory is required");
            return null;
        }

        if (cmd.hasOption("t")) {
            long t;
            try {
                t = Long.parseLong(cmd.getOptionValue("t"));
            } catch (NumberFormatException e) {
                System.err.println("Invalid time: " + cmd.getOptionValue("t"));
                return null;
            }

            if (t <= 0) {
                System.err.println("Max Time must be greater than 0");
                return null;
            }

            builder.t(t);
        } else {
            System.err.println("Max Time is required");
            return null;
        }

        return builder.build();
    }

    public void printHelp() {

        HelpFormatter formatter = new HelpFormatter();
        formatter.setOptionComparator(Comparator.comparingInt(OPTIONS::indexOf));

        formatter.setLeftPadding(4);
        formatter.setWidth(120);

        String commandLineSyntax =
                "java -jar off-lattice-1.0-SNAPSHOT-jar-with-dependencies.jar [options]";

        formatter.printHelp(commandLineSyntax, options);
    }
}
