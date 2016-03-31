/**
 * Created by Hoddi on 16.3.2016.
 */

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

public class Main {

    public static Collection<Relation> putProb(Collection<Relation> rel, Collection<Edge> prob) {

        Collection<Relation> newRel = new ArrayList<>();
        for (int i = 0; i < rel.size(); i++) {
            Relation tmpRel = ((ArrayList<Relation>) rel).get(i);
            for (int k = 0; k < prob.size(); k++) {
                Edge tmpEdge = ((ArrayList<Edge>)prob).get(k);
                if (tmpRel.relation.contains(tmpEdge.name.replaceAll("- /r/", ""))) {
                    tmpRel.prob = tmpEdge.value;
                    newRel.add(tmpRel);
                }
            }
        }
        return newRel;
    }

    public static void main(String[] args) throws IOException, JSONException {

        Collection<JSONArray> test = new ArrayList<>();
        ArrayList<String> list = new ArrayList<>();
        list.add("goat");
        list.add("squirrel");
        list.add("crocodile");
        list.add("chicken");
        list.add("frog");
        list.add("whale");
        list.add("salmon");
        list.add("crab");
        list.add("spider");
        list.add("cow");
        list.add("horse");
        list.add("dog");
        list.add("cat");
        list.add("ant");
        /*
        test = ConceptNet.test("sheep", list);
        for (int i = 0; i < list.size(); i++) {
            System.out.println(((ArrayList<JSONArray>)test).get(i));
        }
        */
        JSONArray arr = ConceptNet.similarityScore("dog", "cat");
        arr.get(0);
        System.out.println(arr);

        System.out.println(arr.get(0));

    }
}
