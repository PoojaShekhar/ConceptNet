import java.util.Comparator;

/**
 * Created by Hoddi on 2.4.2016.
 */
public class EdgeComparator implements Comparator<Edge> {

    @Override
    public int compare(Edge e1, Edge e2) {
        if (e1.value >  e2.value) {
            return -1;
        }
        if (e1.value < e2.value) {
            return 1;
        }
        return 0;
    }
}
