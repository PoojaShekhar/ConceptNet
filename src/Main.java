/**
 * Created by Hoddi on 16.3.2016.
 */

import org.json.JSONException;

import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException, JSONException {

        testConceptNetScores();
        //testConceptNetScore();
        //testAncestorScores();
    }

    public static void testConceptNetScores() throws JSONException, IOException {
        Map<String, Integer> map = new HashMap<>();
        ArrayList<String> list = new ArrayList<>();
        list.add("crab");
        list.add("mouse");
        list.add("dog");
        map = ConceptScore.getTermsScore("bird", list, 1000);
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println("bird: " + entry.getKey() + " " + entry.getValue());
        }
    }

    public static void testConceptNetScore() throws JSONException, IOException{
        Collection<Edge> test = new ArrayList<>();
        ArrayList<String> list = new ArrayList<>();
        list.add("goat");
        list.add("frog");
        //list.add("blueberry");
        try {
            test = ConceptNet.test("sheep", list);
        }
        catch (JSONException ex) {
            System.out.println(ex);
        }
        for (int i = 0; i < test.size(); i++) {
            System.out.println(((ArrayList<Edge>)test).get(i));
        }
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
