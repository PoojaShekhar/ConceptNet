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
    public static Collection<Relation> findConnections(String concept) throws IOException, JSONException {

        Collection<Relation> conceptChildren = new ArrayList<>();
        ArrayList<String> pathList = ConceptEdges.getEdges();
        int queryCount = 50;

        try {
            String conceptQuery = ConceptQuery.returnURL(concept, queryCount);
            JSONObject objQuery = new JSONObject(conceptQuery);


            for (int i = 0; i < queryCount; i++) {

                //try {
                JSONObject objResult = objQuery.getJSONArray("edges").getJSONObject(i);
                JSONArray array = objResult.getJSONArray("features");
                double score = objResult.getDouble("weight");

                for (int k = 0; k < array.length(); k++) {
                    String currArrayItem = array.getString(k);
                    for (int h = 0; h < pathList.size(); h++) {
                        if (currArrayItem.contains(pathList.get(h))) {
                            String conceptChild = currArrayItem;
                            String[] tmp = conceptChild.split(" ");
                            Relation rel = new Relation(tmp[1].replaceAll("/r/", ""), tmp[2].replaceAll("/c/en/", ""), score, 0);
                            if (!conceptChildren.contains(rel)) {
                                conceptChildren.add(rel);
                            }
                        }
                    }
                }
            }
            //catch (JSONException ex) {
            //  break;
            //}
            //return conceptChildren;
        }
        catch (IOException | JSONException ex) {

        }
        return conceptChildren;
    }
}
