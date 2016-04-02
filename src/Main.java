/**
 * Created by Hoddi on 16.3.2016.
 */

import org.json.JSONException;

import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException, JSONException {

        //testConceptNetCount();
        testConceptNetScore();
        //testAncestorScores();

        Chair ch1 = new Chair("chair1", 23.34);
        Chair ch2 = new Chair("chair2", 23.54);
        Chair ch3 = new Chair("chair3", 2.23);
        Chair ch4 = new Chair("chair4", 67.89);

        List<Chair> list = new ArrayList<>();
        list.add(ch1);
        list.add(ch2);
        list.add(ch3);
        list.add(ch4);
        Collections.sort(list, new CompareChair());
        for (int i = 0; i < list.size(); i++) {
            System.out.println(((ArrayList<Chair>)list).get(i));
        }
    }

    public static void testConceptNetCount() throws JSONException, IOException {
        Map<String, Integer> map = new HashMap<>();
        ArrayList<String> list = new ArrayList<>();
        String concept = "bird";
        int limit = 1000;
        list.add("crab");
        list.add("mouse");
        list.add("dog");
        System.out.println("Similarity Count for: " + concept);
        System.out.println("Comparing " + limit + " features");
        map = ConceptScore.getTermsCount(concept, list, limit);
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }

    public static void testConceptNetScore() throws JSONException, IOException{
        Collection<Edge> test = new ArrayList<>();
        ArrayList<String> list = new ArrayList<>();
        String concept = "bird";
        list.add("crab");
        list.add("mouse");
        list.add("dog");
        System.out.println("ConceptNet5 Similarity Score: " + concept);
        test = ConceptNet.getConceptNetScores(concept, list);

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
