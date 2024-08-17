package ar.edu.itba.ss.g2.model;

public class Particle {

    private final Long id;

    private Double x;
    private Double y;

    private Double direction;

    public Particle(Long id, Double x, Double y, Double direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.id = id;
    }

    public Particle(Particle that) {
        this(that.id, that.x, that.y, that.direction);
    }

    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    public Double getDirection() {
        return direction;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public void setDirection(Double direction) {
        this.direction = direction;
    }

    public Long getId() {
        return id;
    }

    public Double distanceTo(Particle other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
    }

    public Double toroidalDistanceTo(Particle other, int L) {
        double dx = Math.abs(this.x - other.x);
        double dy = Math.abs(this.y - other.y);

        // Toroide, me quedo con la distancia más corta
        dx = Math.min(dx, L - dx);
        dy = Math.min(dy, L - dy);

        return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
    }


    @Override
    public String toString() {
        double radians = direction / Math.PI;
        return "{" + "id=" + id + ", x=" + x + ", y=" + y + ", θ=" + radians + "π}";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Particle) {
            Particle p = (Particle) obj;
            return p.id.equals(this.id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
