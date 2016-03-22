import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.LinkedList;

/**
 * Created by Hoddi on 22.3.2016.
 */
public class ConceptParent {

    public static LinkedList<String> findParent(String concept) throws IOException {

        /*
         * parentLength is set to 30 to eliminate large sentences.
         */
        LinkedList<String> conceptParents = new LinkedList<>();
        int queryCount = 30;
        int parentLength = 30;

        String conceptQuery = ConceptQuery.returnURL(concept,queryCount);
        JSONObject objQuery = new JSONObject(conceptQuery);

        for (int i = 0; i < queryCount; i++) {

            JSONObject objResult = objQuery.getJSONArray("edges").getJSONObject(i);
            JSONArray array = objResult.getJSONArray("features");
            for (int k = 0; k < array.length(); k++) {
                String currArrayItem = array.getString(k);
                if (currArrayItem.contains("- /r/IsA")) {
                    String conceptParent = currArrayItem.replaceAll("- /r/IsA","").replaceAll("/c/en/","");
                    conceptParent = "A " + concept + " is a" + conceptParent;
                    if (!conceptParents.contains(conceptParent) && conceptParent.length() < parentLength) {
                        conceptParents.add(conceptParent);
                    }
                }
            }
        }
        return conceptParents;
    }
}
