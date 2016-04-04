/**
 * Created by Hoddi on 28.3.2016.
 */
public class Edge {

    double value;
    String name;

    public Edge (String name, double value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return (name + " " + value);
    }
}
