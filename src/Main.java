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

        Collection<Edge> test = new ArrayList<>();
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

        TreeMap<String,Integer> test2 = new TreeMap<>();
        test2 = ConceptSimilarity.returnSimilar("sheep",list);
        System.out.println(test2);

        Collection<Edge> test3 = new ArrayList<Edge>();
        test3 = ConceptNet.test("sheep", list);
        System.out.println(test3);

        testAncestorScores();
    }

    public static void testAncestorScores() throws IOException, JSONException {
        Set<String> ancs2 = ConceptSimilarity.commonAncestors("sheep", "goat");
        Set<String> ancs3 = ConceptSimilarity.commonAncestors("sheep", "goat", "frog");
        System.out.println("ancestors: " + ancs3);

        int score = 0;
        ArrayList<String> edges = ConceptEdges.getEdges();
        for (String str : ancs2) {
            score += ConceptSimilarity.countDescendants(str, edges);
        }
        System.out.println("Score for sheep, goat: " + score);

        score = 0;
        for (String str : ancs3) {
            score += ConceptSimilarity.countDescendants(str, edges);
        }
        System.out.println("Score for sheep, goat, frog: " + score);
    }
}
