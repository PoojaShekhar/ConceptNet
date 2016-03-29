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
public class Hello {

    /*
     * findConnection lists all possible relations from a concept, to another concept.
     */
    public static Collection<Relation> findConnections(String concept) throws IOException, JSONException {

        Collection<Relation> conceptChildren = new ArrayList<>();
        ArrayList<String> pathList = ConceptEdges.getEdges();
        int queryCount = 300;

        String conceptQuery = ConceptQuery.returnURL(concept,queryCount);
        JSONObject objQuery = new JSONObject(conceptQuery);

        for (int i = 0; i < queryCount; i++) {

            JSONObject objResult = objQuery.getJSONArray("edges").getJSONObject(i);
            JSONArray array = objResult.getJSONArray("features");
            for (int k = 0; k < array.length(); k++) {
                String currArrayItem = array.getString(k);
                for (int h = 0; h < pathList.size(); h++) {
                    if (currArrayItem.contains(pathList.get(h))) {
                        String conceptChild = currArrayItem;
                        String[] test = conceptChild.split(" ");
                        Relation rel = new Relation(test[1].replaceAll("/r/",""), test[2].replaceAll("/c/en/", ""));
                        if (!conceptChildren.contains(rel)) {
                            conceptChildren.add(rel);
                        }
                    }
                }
            }
        }
        return conceptChildren;
    }
}
