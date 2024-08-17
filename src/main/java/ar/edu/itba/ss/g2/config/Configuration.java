package ar.edu.itba.ss.g2.config;

import java.util.Random;

public class Configuration {

    private final String outputDirectory;

    private final int seed;

    private final int N;
    private final int L;

    private final double v;
    private final double noise;
    private final double rc;

    private final long t;

    private Configuration(Builder builder) {
        this.outputDirectory = builder.outputDirectory;
        this.seed = builder.seed;
        this.N = builder.N;

        this.v = builder.v;
        this.noise = builder.noise;
        this.L = builder.L;
        this.rc = builder.rc;
        this.t = builder.t;
    }

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public int getSeed() {
        return seed;
    }

    public int getN() {
        return N;
    }

    public int getL() {
        return L;
    }

    public double getV() {
        return v;
    }

    public double getNoise() {
        return noise;
    }

    public double getRc() {
        return rc;
    }

    public long getT() {
        return t;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "outputDirectory='" + outputDirectory + '\'' +
                ", seed=" + seed +
                ", N=" + N +
                ", L=" + L +
                ", v=" + v +
                ", noise=" + noise +
                ", rc=" + rc +
                ", t=" + t +
                '}';
    }

    public static class Builder {

        private String outputDirectory;

        private int seed = new Random().nextInt();

        private int N;
        private int L;

        private double v;
        private double noise;

        private double rc;

        private long t;

        public Builder() {}

        public Builder seed(int seed) {
            this.seed = seed;
            return this;
        }

        public Builder outputDirectory(String outputDirectory) {
            this.outputDirectory = outputDirectory;
            return this;
        }

        public Builder N(int N) {
            this.N = N;
            return this;
        }

        public Builder L(int L) {
            this.L = L;
            return this;
        }

        public Builder v(double v) {
            this.v = v;
            return this;
        }

        public Builder noise(double noise) {
            this.noise = noise;
            return this;
        }

        public Builder rc(double rc) {
            this.rc = rc;
            return this;
        }

        public Builder t(long t) {
            this.t = t;
            return this;
        }

        public Configuration build() {
            return new Configuration(this);
        }
    }
}
