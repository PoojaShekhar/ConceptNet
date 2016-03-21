import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.LinkedList;

/**
 * Created by Hoddi on 21.3.2016.
 */
public class ConceptChildren {

    public static LinkedList<String> findChildren(String concept) throws IOException {

        LinkedList<String> conceptChildren = new LinkedList<>();
        int queryCount = 30;

        String conceptQuery = ConceptQuery.returnURL(concept,queryCount);
        JSONObject objQuery = new JSONObject(conceptQuery);

        for (int i = 0; i < queryCount; i++) {

            JSONObject objResult = objQuery.getJSONArray("edges").getJSONObject(i);
            JSONArray array = objResult.getJSONArray("features");
            for (int k = 0; k < array.length(); k++) {
                String currArrayItem = array.getString(k);
                if (currArrayItem.contains("IsA -")) {
                    String conceptChild = currArrayItem.replaceAll("/c/en/", "").replaceAll("/r/IsA -", "");
                    conceptChild = "A " + concept + " can be a " + conceptChild;
                    conceptChildren.add(conceptChild);
                }
            }
        }
        return conceptChildren;
    }
}
