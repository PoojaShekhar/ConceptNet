import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeMap;

/**
 * Created by Hoddi on 23.3.2016.
 */
public class ConceptEdges {

    /*
     * For learning edge transition probabilities,
     * to help calculate pathway scoring.
     */
    public static final String atLocationEdge = "AtLocation";
    public static final String relatedToEdge = "RelatedTo";
    public static final String hasAEdge = "HasA";
    public static final String isAEdge = "IsA";
    public static final String capableOfEdge = "CapableOf";
    public static final String desiresEdge = "Desires";
    public static final String usedForEdge = "UsedFor";
    public static final String partOfEdge = "PartOf";
    public static final String hasPropertyEdge = "HasProperty";
    public static final String memberOfEdge = "MemberOf";
    public static final String causesEdge = "Causes";
    public static final String hasSubeventEdge = "HasSubevent";
    public static final String hasFirstSubeventEdge = "HasFirstSubevent";
    public static final String hasLastSubeventEdge = "HasLastSubevent";
    public static final String hasPrerequisiteEdge = "HasPrerequisite";
    public static final String motivatedByGoalEdge = "MotivatedByGoal";
    public static final String obstructedByEdge = "ObstructedBy";
    public static final String createdByEdge = "CreatedBy";
    public static final String synonymEdge = "Synonym";
    public static final String antonymEdge = "Antonym";
    public static final String derivedFromEdge = "DerivedFrom";
    public static final String translationOfEdge = "TranslationOf";
    public static final String definedAsEdge = "DefinedAs";

    public static Collection<String> getEdges() {

        Collection<String> collection = new ArrayList<>();

        collection.add(atLocationEdge);
        collection.add(relatedToEdge);
        collection.add(hasAEdge);
        collection.add(isAEdge);
        collection.add(capableOfEdge);
        collection.add(desiresEdge);
        collection.add(usedForEdge);
        collection.add(partOfEdge);
        collection.add(hasPropertyEdge);
        collection.add(memberOfEdge);
        collection.add(causesEdge);
        collection.add(hasSubeventEdge);
        collection.add(hasFirstSubeventEdge);
        collection.add(hasLastSubeventEdge);
        collection.add(hasPrerequisiteEdge);
        collection.add(motivatedByGoalEdge);
        collection.add(obstructedByEdge);
        collection.add(createdByEdge);
        collection.add(synonymEdge);
        collection.add(antonymEdge);
        collection.add(derivedFromEdge);
        collection.add(translationOfEdge);
        collection.add(definedAsEdge);

        return collection;
    }

    public static TreeMap<String, Integer> getProbabilities(String concept) throws IOException {

        TreeMap<String, Integer> probEdges = new TreeMap<>();
        ArrayList<String> edgeList = new ArrayList<>();
        edgeList.addAll(getEdges());
        int queryCount = 100;
        int count = 0;

        String compareItem = ConceptQuery.returnURL(concept,queryCount);
        JSONObject objCompareItem = new JSONObject(compareItem);

        for (int i = 0; i < queryCount; i++) {
            JSONObject objResult = objCompareItem.getJSONArray("edges").getJSONObject(i);
            JSONArray array = objResult.getJSONArray("features");
            for (int k = 0; k < array.length(); k++) {
                String currArrayItem = array.getString(k);
                for (int j = 0; j < edgeList.size(); j++) {
                    if (currArrayItem.contains(edgeList.get(j))) {
                        count++;
                        if(probEdges.containsKey(edgeList.get(j))) {
                            int intPair = probEdges.get(edgeList.get(j));
                            int intPairCounted = intPair + 1;
                            probEdges.put(edgeList.get(j), intPairCounted);
                        }
                        else
                        {
                            probEdges.put(edgeList.get(j), 1);
                        }

                    }

                }

            }
        }
        return probEdges;
    }

}
