import java.util.Comparator;
import java.util.HashMap;

/*
 * A Comparator class that compares Strings, for sorting the
 * TreeMap<String, Integer> in ConceptSimilarity.java
 */
public class ConceptComparator implements Comparator<String> {

    HashMap<String, Integer> map = new HashMap<String, Integer>();


    public ConceptComparator(HashMap<String, Integer> map){

        this.map.putAll(map);
    }

    @Override
    public int compare(String s1, String s2) {
        if (map.get(s1) >= map.get(s2)){
            return -1;
        }
        else
        {
            return 1;
        }
    }
}