import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by Hoddi on 21.3.2016.
 */
public class ConceptSimilarity {



    /*
     * TreeMap is a map sorted by it's keys.
     * ConceptComparator is used to sort the TreeMap by keys.
     */
    public static TreeMap<String, Integer> sortMapByValue(HashMap<String, Integer> map){
        Comparator<String> comparator = new ConceptComparator(map);
        TreeMap<String, Integer> result = new TreeMap<String, Integer>(comparator);
        result.putAll(map);
        return result;
    }

    public static TreeMap<String, Integer> returnSimilar(String compare, ArrayList<String> compareList) throws IOException, JSONException {

        LinkedList<String> tmpList = new LinkedList<>();
        LinkedList<String> compList = new LinkedList<>();
        HashMap<String, Integer> mapList = new HashMap<>();
        int queryCount = 100;
        int count = 0;

        /*
         * The concept we will compare to the list, compareList.
         */
        String compareItem = ConceptQuery.returnURL(compare,queryCount,0);
        JSONObject objCompareItem = new JSONObject(compareItem);

        for (int k = 0; k < queryCount; k++) {
            JSONObject resItem = objCompareItem.getJSONArray("edges").getJSONObject(k);
            String compItem = resItem.getString("end").replaceAll("/c/en/","");
            if (!compList.contains(compItem)) {
                compList.add(compItem);
            }
        }

        /*
         * Iterate through the compareList, comparing with the chosen concept.
         */
        for (int i = 0; i < compareList.size(); i++) {

            /*
             * The current item from the compareList, compared to the chosen concept.
             */
            String itemList = ConceptQuery.returnURL(compareList.get(i), queryCount,0);
            JSONObject objList = new JSONObject(itemList);

            for (int k = 0; k < queryCount; k++) {
                JSONObject resultList = objList.getJSONArray("edges").getJSONObject(k);
                String currItem = resultList.getString("end").replaceAll("/c/en/", "");
                if (!tmpList.contains(currItem)) {
                    tmpList.add(currItem);
                }
            }

            for (int j = 0; j < tmpList.size(); j++) {
                for (int k = 0; k < compList.size(); k++) {
                    if (tmpList.get(j).equals(compList.get(k))) {
                        count++;
                    }
                }
            }
            /*
             * Adding the current compareList item and it's match count to the mapList HashMap.
             */
            mapList.put(compareList.get(i), count);
            count = 0;
            tmpList.clear();
        }
        TreeMap<String, Integer> sortedMap = sortMapByValue(mapList);

        return sortedMap;
    }

    public static Set<ConceptNode> commonAncestors (String[] concepts, String concept) throws IOException, JSONException {
        Set<ConceptNode> commonAncestors = new HashSet<>();
        ArrayList<String> edges = ConceptEdges.getEdgesTest();

        ArrayList<Set<ConceptNode>> ancestorList = new ArrayList<>();
        for (int i = 0; i<concepts.length; i++) {
            ancestorList.add(new HashSet<>(getAncestors(null, concepts[i], edges)));
        }

        Set<ConceptNode> tmp = new HashSet<>();

        int depth = 1;
        while(depth < 4) {
            System.out.println("Depth: " + depth);

            // Check for ancestors between two concepts
            for (int i = 0; i < ancestorList.size(); i++) {
                for (int j = i+1; j < ancestorList.size(); j++) {
                    for (ConceptNode node : ancestorList.get(j)) {
                        if (!commonAncestors.contains(node) && ancestorList.get(i).contains(node)) {
                            System.out.println("Found ancestor: " + node.toString());
                            commonAncestors.add(node);
                        }
                    }

                }
            }

            // Expand to the next level
            for (int i = 0; i < ancestorList.size(); i++) {
                tmp.clear();
                for (ConceptNode anc : ancestorList.get(i)) {
                    if (!anc.start.equals("animal"))
                        tmp.addAll(getAncestors(anc, anc.end, edges));
                }
                ancestorList.get(i).addAll(tmp);
            }

            depth++;
        }

        for (ConceptNode node : commonAncestors) {
            if (node.end.equals("animal")) {
                commonAncestors.remove(node);
                break;
            }
        }
        return commonAncestors;
    }


    public static ArrayList<ConceptNode> getAncestors (ConceptNode parent, String concept, ArrayList<String> edges) throws IOException, JSONException {
        ArrayList<ConceptNode> ancestors = new ArrayList<>();

        JSONObject conceptObj = ConceptQuery.getJSONObject(concept, 100,0);
        JSONArray objArray = conceptObj.getJSONArray("edges");

        for (int i = 0; i<objArray.length(); i++) {
            String start = objArray.getJSONObject(i).get("start").toString();
            String rel = objArray.getJSONObject(i).get("rel").toString();
            String end = objArray.getJSONObject(i).get("end").toString();
            double weight = objArray.getJSONObject(i).getDouble("weight");
            if (edges.contains(rel) /*&& start.equals("/c/en/" + concept)*/ /*&& weight > 1.6*/) {
                ancestors.add(new ConceptNode(parent, start.replaceAll("/c/en/","").replaceAll("/n/.*", ""), rel, end.replaceAll("/c/en/","").replaceAll("/n/.*", ""), weight));
            }
        }

        return ancestors;
    }

    public static ArrayList<ConceptNode> getDescendants (ConceptNode parent, String concept, ArrayList<String> edges) throws IOException, JSONException {
        ArrayList<ConceptNode> ancestors = new ArrayList<>();

        JSONObject conceptObj = ConceptQuery.getJSONObject(concept, 100,0);
        JSONArray objArray = conceptObj.getJSONArray("edges");

        for (int i = 0; i<objArray.length(); i++) {
            String start = objArray.getJSONObject(i).get("start").toString();
            String rel = objArray.getJSONObject(i).get("rel").toString();
            String end = objArray.getJSONObject(i).get("end").toString();
            double weight = objArray.getJSONObject(i).getDouble("weight");
            if (edges.equals(rel) && start.equals("/c/en/" + concept) && weight > 1.6) {
                ancestors.add(new ConceptNode(parent, start.replaceAll("/c/en/",""), rel, end.replaceAll("/c/en/",""), weight));
            }
        }

        return ancestors;
    }

    public static int countDescendants (String concept, ArrayList<String> edges) throws IOException, JSONException {
        int count = 0;

        JSONObject conceptObj = ConceptQuery.getJSONObject(concept, 1000,0);
        JSONArray objArray = conceptObj.getJSONArray("edges");

        for (int i = 0; i<objArray.length(); i++) {
            String rel = objArray.getJSONObject(i).get("rel").toString();
            String end = objArray.getJSONObject(i).get("end").toString();
            double weight = objArray.getJSONObject(i).getDouble("weight");
            if (edges.contains(rel) && end.equals("/c/en/" + concept) /*&& weight > 1.6*/) {
                count++;
            }
        }

        return count;
    }
}
