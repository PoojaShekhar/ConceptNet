import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Hoddi on 23.3.2016.
 */
public class ConceptEdges {

    /*
     * For learning edge transition probabilities,
     * to help calculate pathway scoring.
     * Not Complete. Need to find a path from a concept to concept,
     * with the probability scoring? 
     */
    public static final String atLocationEdge = "/r/AtLocation";
    public static final String relatedToEdge = "/r/RelatedTo";
    public static final String hasAEdge = "/r/HasA";
    public static final String isAEdge = "/r/IsA";
    public static final String capableOfEdge = "/r/CapableOf";
    public static final String desiresEdge = "/r/Desires";
    public static final String usedForEdge = "/r/UsedFor";
    public static final String partOfEdge = "/r/PartOf";
    public static final String hasPropertyEdge = "/r/HasProperty";
    public static final String memberOfEdge = "/r/MemberOf";
    public static final String causesEdge = "/r/Causes";
    public static final String hasSubeventEdge = "/r/HasSubevent";
    public static final String hasFirstSubeventEdge = "/r/HasFirstSubevent";
    public static final String hasLastSubeventEdge = "/r/HasLastSubevent";
    public static final String hasPrerequisiteEdge = "/r/HasPrerequisite";
    public static final String motivatedByGoalEdge = "/r/MotivatedByGoal";
    public static final String obstructedByEdge = "/r/ObstructedBy";
    public static final String createdByEdge = "/r/CreatedBy";
    public static final String synonymEdge = "/r/Synonym";
    public static final String antonymEdge = "/r/Antonym";
    public static final String derivedFromEdge = "/r/DerivedFrom";
    public static final String translationOfEdge = "/r/TranslationOf";
    public static final String definedAsEdge = "/r/DefinedAs";

    public static ArrayList<String> getEdges() {

        ArrayList<String> collection = new ArrayList<>();

        collection.add(atLocationEdge);
        //collection.add(relatedToEdge);
        collection.add(hasAEdge);
        collection.add(isAEdge);
        collection.add(capableOfEdge);
        collection.add(desiresEdge);
        collection.add(usedForEdge);
        collection.add(partOfEdge);
        collection.add(hasPropertyEdge);
        collection.add(memberOfEdge);
        //collection.add(causesEdge);
        //collection.add(hasSubeventEdge);
        //collection.add(hasFirstSubeventEdge);
        //collection.add(hasLastSubeventEdge);
        //collection.add(hasPrerequisiteEdge);
        collection.add(motivatedByGoalEdge);
        collection.add(obstructedByEdge);
        collection.add(createdByEdge);
        collection.add(synonymEdge);
        collection.add(antonymEdge);
        collection.add(derivedFromEdge);
        //collection.add(translationOfEdge);
        collection.add(definedAsEdge);

        return collection;
    }

    /*
     * Used to convert TreeMap<String, Integer> to a Collection of Edge(String, Double).
     */
    public static Collection<Edge> makeCollection(TreeMap<String, Integer> map, int count) {
        Collection<Edge> coll = new ArrayList<>();
        for(Map.Entry<String,Integer> entry : map.entrySet()) {
            String key = entry.getKey();
            Double value = (double)entry.getValue();
            Edge edge = new Edge(key, value/count);
            coll.add(edge);
        }
        return coll;
    }

    /*
     * Make a Collection<Edge> that shows the probabilities of different edges leading
     * from or to a concept.
     */
    public static Collection<Edge> getProbabilities(String concept) throws IOException, JSONException {

        TreeMap<String, Integer> probEdges = new TreeMap<>();
        ArrayList<String> edgeList = new ArrayList<>();
        edgeList.addAll(getEdges());
        int queryCount = 300;
        int count = 0;

        String compareItem = ConceptQuery.returnURL(concept,queryCount);
        JSONObject objCompareItem = new JSONObject(compareItem);

        for (int i = 0; i < queryCount; i++) {
            try {
                JSONObject objResult = objCompareItem.getJSONArray("edges").getJSONObject(i);
                JSONArray array = objResult.getJSONArray("features");
                for (int k = 0; k < array.length(); k++) {
                    String currArrayItem = array.getString(k);
                    for (int j = 0; j < edgeList.size(); j++) {
                        if (currArrayItem.contains(edgeList.get(j)) && !currArrayItem.contains(concept)) {
                            count++;
                            if (probEdges.containsKey(edgeList.get(j))) {
                                int intPair = probEdges.get(edgeList.get(j));
                                int intPairCounted = intPair + 1;
                                probEdges.put(edgeList.get(j), intPairCounted);
                            } else {
                                probEdges.put(edgeList.get(j), 1);
                            }

                        }

                    }

                }
            }
            catch (JSONException ex) {

            }
        }
        return makeCollection(probEdges, count);
    }

}
