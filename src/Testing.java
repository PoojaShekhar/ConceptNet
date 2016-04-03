import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Hoddi on 3.4.2016.
 */
public class Testing {

    public static Collection<Node> getNodes(String concept, int limit, int searchSpace, boolean searchDown) throws JSONException, IOException {

        String conceptChild = "";
        String conceptRelation = "";
        String conceptParent = " ";
        double conceptWeight = 0;
        Collection<Node> nodeCollection = new ArrayList<>();
        int start = 0;
        int queryIncrementer = 0;

        JSONObject obj = ConceptQuery.getJSONObject(concept, limit, queryIncrementer);
        JSONArray array = obj.getJSONArray("edges");
        System.out.println("Fetching " + limit + " Nodes:");
        if (searchDown) {
            System.out.println("Searching Children Of: " + concept);
        }
        else {
            System.out.println("Searching Parents Of: " + concept);
        }
        while (searchSpace > queryIncrementer*limit) {
            for (int i = 0; i < array.length(); i++) {
                conceptParent = array.getJSONObject(i).getString("start");
                conceptChild = array.getJSONObject(i).getString("end");
                conceptRelation = array.getJSONObject(i).getString("rel");
                conceptWeight = array.getJSONObject(i).getDouble("weight");
                if (searchDown) {
                    if (concept.equals(conceptParent.replace("/c/en/",""))) {
                        if (conceptChild.contains("/c/en/") && conceptParent.contains("/c/en")) {
                            if (conceptChild.length() < 30 && conceptParent.length() < 30) {
                                Node node = new Node(conceptParent.replace("/c/en/", ""), conceptChild.replace("/c/en/", ""), conceptRelation.replace("/r/", ""), conceptWeight);
                                if (!nodeCollection.contains(node) && !(nodeCollection.size() == limit)) {
                                    nodeCollection.add(node);
                                }
                            }
                        }
                    }
                }
                if (!searchDown) {
                    if (concept.equals(conceptChild.replace("/c/en/", ""))) {
                        if (conceptChild.contains("/c/en/") && conceptParent.contains("/c/en")) {
                            if (conceptChild.length() < 30 && conceptParent.length() < 30) {
                                Node node = new Node(conceptParent.replace("/c/en/", ""), conceptChild.replace("/c/en/", ""), conceptRelation.replace("/r/", ""), conceptWeight);
                                if (!nodeCollection.contains(node) && !(nodeCollection.size() == limit)) {
                                    nodeCollection.add(node);
                                }
                            }
                        }
                    }
                }
            }
            int missing = limit - nodeCollection.size();
            for (int i = start; i < nodeCollection.size(); i++) {
                System.out.println(((ArrayList<Node>)nodeCollection).get(i));
            }
            if (missing <= 0) {
                System.out.println("Found " + nodeCollection.size() + " Nodes.");
                return nodeCollection;
            }
            else {
                queryIncrementer++;
                obj = ConceptQuery.getJSONObject(concept, limit, limit*queryIncrementer);
                array = obj.optJSONArray("edges");
                start = limit - missing;
            }
        }
        System.out.println("Found " + nodeCollection.size() + " Nodes from " + limit*queryIncrementer + " results.");
        return nodeCollection;
    }

    public static Collection<Collection<Node>> getNodeChildrens(String concept, int limit, int branching) throws JSONException, IOException {

        Collection<Collection<Node>> connectionsCollection = new ArrayList<>();
        Collection<Node> nodes = new ArrayList<>();
        Collection<Node> nodesTmp = new ArrayList<>();
        Collection<Node> nodeChildren = new ArrayList<>();

        nodes = getNodes(concept,limit, 100, true);
        while (true) {
            for (Node oldNode : nodes) {
                nodesTmp = getNodes(oldNode.child, branching, 100, true);
                nodeChildren.add(oldNode);
                for (Node nextNode : nodesTmp) {
                    if (oldNode.child.equals(nextNode.parent)) {
                        Node newNode = new Node(oldNode.child, nextNode.child, nextNode.relation, nextNode.weight);
                        nodeChildren.add(newNode);
                    }
                }
            }
            System.out.println("Printing connections");
            connectionsCollection.add(nodeChildren);
            return connectionsCollection;
        }
    }
}
