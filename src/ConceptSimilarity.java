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
        String compareItem = ConceptQuery.returnURL(compare,queryCount);
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
            String itemList = ConceptQuery.returnURL(compareList.get(i), queryCount);
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

    public static Set<String> commonAncestors (String concept1, String concept2, String concept3) throws IOException, JSONException {
        System.out.println("commonAncestors3:");
        Set<String> commonAncestors = new HashSet<>();

        ArrayList<ConceptNode> ancestors1 = getAncestors("null", concept1);
        ArrayList<ConceptNode> ancestors2 = getAncestors("null", concept2);
        ArrayList<ConceptNode> ancestors3 = getAncestors("null", concept3);

        Set<ConceptNode> tmp = new HashSet<>();

        int depth = 0;
        while(depth < 4) {
            depth++;
            System.out.println("Depth: " + depth);

            // Check for any ancestors
            for (ConceptNode x : ancestors1) {
                for (ConceptNode y : ancestors2) {
                    for (ConceptNode z : ancestors3) {
                        if (x.end.equals(y.end) && x.end.equals(z.end)) {
                            if (!commonAncestors.contains(x.end)) {
                                System.out.println("Found ancestor: " + x.end + "; { (" + x.start + " " + x.rel + ", p: " + x.parent
                                                                                + "), (" + y.start + " " + y.rel + ", p: " + y.parent
                                                                                + "), (" + z.start + " " + z.rel + ", p: " + z.parent + ") }");
                                commonAncestors.add(x.end);
                            }
                        }
                    }
                }
            }

            // expand first concept
            tmp.clear();
            for (ConceptNode anc : ancestors1) {
                tmp.addAll(getAncestors(anc.start, anc.end));
            }
            ancestors1.addAll(tmp);

            // expand second concept
            tmp.clear();
            for (ConceptNode anc : ancestors2) {
                tmp.addAll(getAncestors(anc.start, anc.end));
            }
            ancestors2.addAll(tmp);

            // expand third concept
            tmp.clear();
            for (ConceptNode anc : ancestors3) {
                tmp.addAll(getAncestors(anc.start, anc.end));
            }
            ancestors3.addAll(tmp);
        }

        return commonAncestors;
    }

    public static Set<String> commonAncestors (String concept1, String concept2) throws IOException, JSONException {
        System.out.println("commonAncestors2:");
        Set<String> commonAncestors = new HashSet<>();

        ArrayList<ConceptNode> ancestors1 = getAncestors("null", concept1);
        ArrayList<ConceptNode> ancestors2 = getAncestors("null", concept2);

        Set<ConceptNode> tmp = new HashSet<>();

        int depth = 0;
        while(depth < 4) {
            depth++;
            System.out.println("Depth: " + depth);

            // Check for any ancestors
            for (ConceptNode x : ancestors1) {
                for (ConceptNode y : ancestors2) {
                    if (x.end.equals(y.end)) {
                        if (!commonAncestors.contains(x.end)) {
                            System.out.println("Found ancestor: " + x.end + "; { (" + x.start + " " + x.rel + ", p: " + x.parent
                                                                        + "), (" + y.start + " " + y.rel + ", p: " + y.parent + ") }");
                            commonAncestors.add(x.end);
                        }
                    }
                }
            }

            // expand first concept
            tmp.clear();
            for (ConceptNode anc : ancestors1) {
                tmp.addAll(getAncestors(anc.start, anc.end));
            }
            ancestors1.addAll(tmp);

            // expand second concept
            tmp.clear();
            for (ConceptNode anc : ancestors2) {
                tmp.addAll(getAncestors(anc.start, anc.end));
            }
            ancestors2.addAll(tmp);
        }

        return commonAncestors;
    }


    public static ArrayList<ConceptNode> getAncestors (String parent, String concept) throws IOException, JSONException {
        ArrayList<ConceptNode> ancestors = new ArrayList<>();

        JSONObject conceptObj = ConceptQuery.getJSONObject(concept, 100);
        JSONArray objArray = conceptObj.getJSONArray("edges");

        for (int i = 0; i<objArray.length(); i++) {
            String start = objArray.getJSONObject(i).get("start").toString();
            String rel = objArray.getJSONObject(i).get("rel").toString();
            String end = objArray.getJSONObject(i).get("end").toString();
            double weight = objArray.getJSONObject(i).getDouble("weight");
            if (rel.equals("/r/IsA") && start.equals("/c/en/" + concept) && weight > 1.6) {
                ancestors.add(new ConceptNode(parent, start.replaceAll("/c/en/",""), rel, end.replaceAll("/c/en/","")));
            }
        }

        return ancestors;
    }

    public static ArrayList<ConceptNode> getDescendants (String parent, String concept, ArrayList<String> edges) throws IOException, JSONException {
        ArrayList<ConceptNode> ancestors = new ArrayList<>();

        JSONObject conceptObj = ConceptQuery.getJSONObject(concept, 100);
        JSONArray objArray = conceptObj.getJSONArray("edges");

        for (int i = 0; i<objArray.length(); i++) {
            String start = objArray.getJSONObject(i).get("start").toString();
            String rel = objArray.getJSONObject(i).get("rel").toString();
            String end = objArray.getJSONObject(i).get("end").toString();
            double weight = objArray.getJSONObject(i).getDouble("weight");
            if (edges.equals(rel) && start.equals("/c/en/" + concept) && weight > 1.6) {
                ancestors.add(new ConceptNode(parent, start.replaceAll("/c/en/",""), rel, end.replaceAll("/c/en/","")));
            }
        }

        return ancestors;
    }

    public static int countDescendants (String concept, ArrayList<String> edges) throws IOException, JSONException {
        int count = 0;

        JSONObject conceptObj = ConceptQuery.getJSONObject(concept, 100000);
        JSONArray objArray = conceptObj.getJSONArray("edges");

        for (int i = 0; i<objArray.length(); i++) {
            String rel = objArray.getJSONObject(i).get("rel").toString();
            String end = objArray.getJSONObject(i).get("end").toString();
            double weight = objArray.getJSONObject(i).getDouble("weight");
            if (edges.contains(rel) && end.equals("/c/en/" + concept) && weight > 1.6) {
                count++;
            }
        }

        return count;
    }
}
