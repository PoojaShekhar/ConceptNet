/**
 * Created by Hoddi on 2.4.2016.
 */
public class Chair {

    private String weight;
    private double height;

    public Chair(String weight, double height) {
        this.weight = weight;
        this.height = height;
    }

    public double getHeight() {
        return this.height;
    }

    public String getWeight() {
        return this.weight;
    }

    public String toString() {
        return weight + " " + height;
    }
}
