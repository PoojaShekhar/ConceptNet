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

    /*
     * Search for parent or children from a specific concept.
     * Tries to fetch the exact amount of children or parents, specified in limit,
     * by incrementing the search query if it can't find the required amount in the first query.
     */

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
                            if (conceptChild.length() < 30 && conceptParent.length() < 30 && conceptWeight > 1.6) {
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
                            if (conceptChild.length() < 30 && conceptParent.length() < 30 && conceptWeight > 1.6) {
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

    public static Collection<Node> getNodeChildrens(String concept, int limit, int searchSpace) throws JSONException, IOException {

        Collection<Node> connectionsCollection = new ArrayList<>();
        Collection<Node> nodes = new ArrayList<>();
        Collection<Node> nodesTmp = new ArrayList<>();
        Collection<Node> nodesTmp2 = new ArrayList<>();
        Collection<Node> nodesTmp3 = new ArrayList<>();
        Collection<Node> nodesTmp4 = new ArrayList<>();
        Collection<Node> nodesTmp5 = new ArrayList<>();

        nodes = getNodes(concept, limit, searchSpace, true);
        for (Node oldNodes : nodes) {
            connectionsCollection.add(oldNodes);
            System.out.println("Children of " + oldNodes.child);
            nodesTmp = getNodes(oldNodes.child, limit, searchSpace, true);
            for (Node newNode: nodesTmp) {
                connectionsCollection.add(newNode);
                System.out.println("Children of " + newNode.child);
                nodesTmp2 = getNodes(newNode.child, limit, searchSpace, true);
                for (Node newestNode : nodesTmp2) {
                    connectionsCollection.add(newestNode);
                    System.out.println("Children of " + newestNode.child);
                    nodesTmp3 = getNodes(newestNode.child, limit, searchSpace, true);
                    for (Node newest2Node : nodesTmp3) {
                        connectionsCollection.add(newest2Node);
                        System.out.println("Children of " + newest2Node.child);
                        nodesTmp4 = getNodes(newest2Node.child, limit, searchSpace, true);
                        for (Node newest3Node : nodesTmp4) {
                            connectionsCollection.add(newest3Node);
                            System.out.println("Children of " + newest3Node.child);
                            nodesTmp5 = getNodes(newest3Node.child, limit, searchSpace, true);
                        }
                    }
                }
            }
        }
        System.out.println("----------------------");
        System.out.println("RETURNING CONNECTIONS FOR " + concept);
        System.out.println("-----------------------");
        return connectionsCollection;
    }
}
