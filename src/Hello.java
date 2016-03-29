import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by HörðurMár on 29.3.2016.
 */
public class Hello {

    public static Collection<Relation> findChildren(String concept) throws IOException, JSONException {

        Collection<Relation> conceptChildren = new LinkedList<>();
        ArrayList<String> test = ConceptEdges.getEdges();
        int queryCount = 300;

        String conceptQuery = ConceptQuery.returnURL(concept,queryCount);
        JSONObject objQuery = new JSONObject(conceptQuery);

        for (int i = 0; i < queryCount; i++) {

            JSONObject objResult = objQuery.getJSONArray("edges").getJSONObject(i);
            JSONArray array = objResult.getJSONArray("features");
            for (int k = 0; k < array.length(); k++) {
                String currArrayItem = array.getString(k);
                for (int h = 0; h < test.size(); h++) {
                    if (currArrayItem.contains(test.get(h))) {
                        String conceptChild = currArrayItem;
                        String[] test2 = conceptChild.split(" ");
                        Relation test3 = new Relation(test2[1], test2[2]);
                        if (!conceptChildren.contains(test3.hashCode())) {
                            conceptChildren.add(test3);
                        }
                    }
                }
            }
        }
        return conceptChildren;
    }
}
