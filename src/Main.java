/**
 * Created by Hoddi on 16.3.2016.
 */

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException, JSONException {

        //testConceptNetCount();
        //testConceptNetScore();
        //testAncestorScores();
        //test();
        //test2();
        //testCountAncestorScores();
        findConnection();

    }


    /*
     * Find a link between two concepts in a directed graph.
     * 
     */
    public static void findConnection() throws JSONException, IOException {
        Collection<Node> coll = new ArrayList<>();
        coll = Final.showRelations("house", "space_shuttle");
    }

    public static void test2() throws IOException{

        File file = new File("animal.txt");
        Scanner scanner = new Scanner(file);
        Collection<Node> nodeColl = new ArrayList<>();
        Collection<Node> nodeRel = new ArrayList<>();

        while (scanner.hasNext()) {
            String parent = scanner.next();
            String relation = scanner.next();
            String child = scanner.next();
            Double weight = Double.parseDouble(scanner.next());
            Node newNode = new Node(parent, child, relation, weight);
            nodeColl.add(newNode);
        }

        for (int i = 0; i < nodeColl.size(); i++) {
            System.out.println(((ArrayList<Node>)nodeColl).get(i));
        }
        System.out.println(nodeColl.size());

        Collection<Node> coll = new ArrayList<>();
        coll.add(((ArrayList<Node>)nodeColl).get(0));
        String tmp = ((ArrayList<Node>)nodeColl).get(2).child;
        String tmp2 = ((ArrayList<Node>)nodeColl).get(3).parent;

        for (int i = 0; i < nodeColl.size(); i++) {
            if (tmp.equals(((ArrayList<Node>) nodeColl).get(i).parent)) {
                coll.add(((ArrayList<Node>) nodeColl).get(i));
                tmp = ((ArrayList<Node>) nodeColl).get(i).parent;
            }
        }

        for (int i = 0; i < coll.size(); i++) {
            System.out.println(((ArrayList<Node>)coll).get(i));
        }
        System.out.println(tmp);
        System.out.println(tmp2);
    }

    public static void test() throws JSONException, IOException {

        Collection<Node> test = new ArrayList<>();
        test = Testing.getNodeChildrens("animal", 10, 100);
        for (int i = 0; i < test.size(); i++) {
            System.out.println(((ArrayList<Node>)test).get(i));
        }
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
