import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by Hoddi on 29.3.2016.
 */
public class Cougar {

    /*
     * findConnection lists all possible relations from one concept, and the concept connected to that rel.
     * Also lists the ConceptNet5 user score, weight, that this rel holds.
     */
    public static Collection<Node> findConnections(String concept) throws IOException, JSONException {

        Collection<Node> conceptChildren = new ArrayList<>();
        ArrayList<String> pathList = ConceptEdges.getEdges();
        int queryCount = 30;

        try {
            String conceptQuery = ConceptQuery.returnURL(concept, queryCount,0);
            JSONObject objQuery = new JSONObject(conceptQuery);


            for (int i = 0; i < queryCount; i++) {

                //try {
                JSONObject objResult = objQuery.getJSONArray("edges").getJSONObject(i);
                String relation = objResult.getString("rel");
                double score = objResult.getDouble("weight");
                String parent = objResult.getString("start");
                String child = objResult.getString("end");

                if (pathList.contains(relation)) {
                    Node node = new Node(parent.replace("/c/en/", ""), child.replace("/c/en/", ""), relation.replace("/r/",""), score);
                    if (!conceptChildren.contains(node)) {
                        conceptChildren.add(node);
                    }
                }

            }
        }
        catch (IOException | JSONException ex) {

        }
        return conceptChildren;
    }
}
