/**
 * Created by Hoddi on 16.3.2016.
 */

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException, JSONException {

        //testConceptNetCount();
        //testConceptNetScore();
        //testAncestorScores();
        //testCountAncestorScores();
        findConnection();

    }

    /*
     * Find the shortest link between two concepts in a directed graph.
     */
    public static void findConnection() throws JSONException, IOException {
        Collection<Node> coll = new ArrayList<>();
        coll = Final.showRelations("house", "animal", true);
    }

    public static void testConceptNetCount() throws JSONException, IOException {
        Map<String, Integer> map = new HashMap<>();
        ArrayList<String> list = new ArrayList<>();
        String concept = "sheep";
        int limit = 1000;
        list.add("crab");
        list.add("mouse");
        list.add("dog");
        list.add("goat");
        list.add("cow");
        list.add("parrot");
        list.add("car");
        list.add("apple");

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
        int score = 0;
        ArrayList<String> edges = ConceptEdges.getEdges();

        Set<ConceptNode> ancs = ConceptSimilarity.commonAncestors(new String[]{"sheep", "lion"});
        Set<String> ancStr = new HashSet<>();
        for (ConceptNode node : ancs)
            ancStr.add(node.end);
        System.out.println("ancestors " + ancs.size() + ": " + ancStr);

        System.out.println("paths?");
        for (ConceptNode node : ancs) {
            ConceptNode nice = node;
            while (nice != null) {
                System.out.println(nice.toString());
                nice = nice.parent;
            }
            System.out.println();
        }

        System.out.println();
        ancs = ConceptSimilarity.commonAncestors(new String[]{"goat", "sheep"});
        ancStr.clear();
        for (ConceptNode node : ancs)
            ancStr.add(node.end);
        System.out.println("ancestors " + ancs.size() + ": " + ancStr);

        System.out.println("paths?");
        for (ConceptNode node : ancs) {
            ConceptNode nice = node;
            while (nice != null) {
                System.out.println(nice.toString());
                nice = nice.parent;
            }
            System.out.println();
        }
    }

    public static void testCountAncestorScores() throws IOException, JSONException {
        ArrayList<String> edges = ConceptEdges.getEdges();
        String compareToConcept = "sheep";

        String[] concepts = {
                "goat",
                "cow",
                "horse",
                "dog",
                "squirrel",
                "frog",
                "crocodile",
                "puffin",
                "chicken",
                "ant",
                "crab",
                "whale",
                "spider",
                "cobra",
                "salmon"
        };
        System.out.println("Finding common ancestors...");
        Map<String, Set<ConceptNode>> ancs = ConceptSimilarity.commonAncConcept(concepts, compareToConcept);

        Map<String, Set<String>> ancStr = new HashMap<>();
        for (String s : ancs.keySet()) {
            ancStr.put(s, new HashSet<>());
            for (ConceptNode node : ancs.get(s)) {
                ancStr.get(s).add(node.end);
            }
        }
        for (String s : ancStr.keySet()) {
            System.out.println(s + ": " + ancStr.get(s));
        }


        // Count it!
        System.out.println("Similarity counting...");
        int limit = 5000;

        System.out.println("Counting attributes for terms...");
        Map<String, Integer> map = ConceptSimilarity.countCommonAttrConcept(concepts, compareToConcept);

        System.out.println("Counting common ancestor attributes...");
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            for (String s : ancStr.get(entry.getKey())) {
                entry.setValue(entry.getValue() + ConceptSimilarity.countAttributes(s, limit, edges));
            }
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }


    }
}
