/**
 * Created by Hoddi on 16.3.2016.
 */

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException, JSONException {

        //testConceptNetCount();
        //testConceptNetScore();
        //testAncestorScores();
        //test();
        test2();

    }

    public static void test2() throws IOException{

        File file = new File("C:\\Users\\Hoddi\\Desktop\\animal.txt");
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

        for (int k = 0; k < nodeColl.size(); k++) {
            if (((ArrayList<Node>)nodeColl).get(2).child.equals(((ArrayList<Node>)nodeColl).get(k).parent)) {
                nodeRel.add(((ArrayList<Node>)nodeColl).get(0));
                nodeRel.add(((ArrayList<Node>)nodeColl).get(k));
            }
        }

        System.out.println(((ArrayList<Node>)nodeColl).get(0).child);
        System.out.println(((ArrayList<Node>)nodeColl).get(3).parent);
        for (int i = 0; i < nodeRel.size(); i++) {
            System.out.println(((ArrayList<Node>)nodeRel).get(i));
        }

        for (int i = 0; i < nodeColl.size(); i++) {
            System.out.println(((ArrayList<Node>)nodeColl).get(i));
        }
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
