import java.util.Comparator;

/**
 * Created by Hoddi on 2.4.2016.
 */
public class CompareChair  implements Comparator<Chair> {

    public int compare(Chair ch1, Chair ch2) {
        if (ch1.getHeight() < ch2.getHeight()) {
            return 1;
        }
        if (ch1.getHeight() > ch2.getHeight()) {
            return -1;
        }
        return 0;
    }
}
