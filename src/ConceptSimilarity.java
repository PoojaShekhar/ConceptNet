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

    public static Set<ConceptNode> commonAncestors (String[] concepts) throws IOException, JSONException {
        Set<ConceptNode> commonAncestors = new HashSet<>();
        ArrayList<String> edges = ConceptEdges.getEdgesTest();

        ArrayList<Set<ConceptNode>> ancestorList = new ArrayList<>();
        for (int i = 0; i<concepts.length; i++) {
            ancestorList.add(new HashSet<>(getAncestors(null, concepts[i])));
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
                        tmp.addAll(getAncestors(anc, anc.end));
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

    public static Map<String, Set<ConceptNode>> commonAncConcept (String[] concepts, String concept) throws IOException, JSONException {
        Map<String, Set<ConceptNode>> commonAncestors = new HashMap<>();
        ArrayList<String> edges = ConceptEdges.getEdgesTest();

        Set<ConceptNode> conceptAncs = new HashSet<>();
        conceptAncs.addAll(getAncestors(null, concept));

        Map<String, Set<ConceptNode>> ancestorMap = new HashMap<>();
        for (int i = 0; i<concepts.length; i++) {
            ancestorMap.put(concepts[i], new HashSet<>(getAncestors(null, concepts[i])));
            commonAncestors.put(concepts[i], new HashSet<>());
        }

        Set<ConceptNode> tmp = new HashSet<>();

        int depth = 1;
        while(depth < 4) {
            System.out.println("Depth: " + depth);

            // Check for ancestors between two concepts
            for (String s : ancestorMap.keySet()) {
                for (ConceptNode node : conceptAncs) {
                    if (!commonAncestors.get(s).contains(node) && ancestorMap.get(s).contains(node)) {
                        System.out.println("Found ancestor for " + s + ": " +node.toString());
                        commonAncestors.get(s).add(node);
                    }
                }
            }

            // Expand to the next level
            for (String s : ancestorMap.keySet()) {
                tmp.clear();
                for (ConceptNode anc : ancestorMap.get(s)) {
                    if (!anc.start.equals("animal") && !anc.end.equals("animal"))
                        tmp.addAll(getAncestors(anc, anc.end));
                }
                ancestorMap.get(s).addAll(tmp);
            }

            depth++;
        }


        // Remove animal
        for (String s : ancestorMap.keySet()) {
            for (ConceptNode node : commonAncestors.get(s)) {
                if (node.end.equals("animal")) {
                    commonAncestors.get(s).remove(node);
                    break;
                }
            }
        }
        return commonAncestors;
    }


    public static ArrayList<ConceptNode> getAncestors (ConceptNode parent, String concept) throws IOException, JSONException {
        ArrayList<ConceptNode> ancestors = new ArrayList<>();

        JSONObject conceptObj = ConceptQuery.getJSONObject(concept, 100,0);
        JSONArray objArray = conceptObj.getJSONArray("edges");

        for (int i = 0; i<objArray.length(); i++) {
            String start = objArray.getJSONObject(i).get("start").toString();
            String rel = objArray.getJSONObject(i).get("rel").toString();
            String end = objArray.getJSONObject(i).get("end").toString();
            double weight = objArray.getJSONObject(i).getDouble("weight");
            if (rel.contains("/r/IsA") /*&& start.equals("/c/en/" + concept)*/ /*&& weight > 1.6*/) {
                ancestors.add(new ConceptNode(parent, start.replaceAll("/c/en/","").replaceAll("/n/.*", ""), rel, end.replaceAll("/c/en/","").replaceAll("/n/.*", ""), weight));
            }
        }

        return ancestors;
    }

    public static ArrayList<ConceptNode> getAttributes (ConceptNode parent, String concept, ArrayList<String> edges) throws IOException, JSONException {
        ArrayList<ConceptNode> attributes = new ArrayList<>();

        JSONObject conceptObj = ConceptQuery.getJSONObject(concept, 100,0);
        JSONArray objArray = conceptObj.getJSONArray("edges");

        for (int i = 0; i<objArray.length(); i++) {
            String start = objArray.getJSONObject(i).get("start").toString();
            String rel = objArray.getJSONObject(i).get("rel").toString();
            String end = objArray.getJSONObject(i).get("end").toString();
            double weight = objArray.getJSONObject(i).getDouble("weight");
            if (edges.contains(rel) /*&& start.equals("/c/en/" + concept)*/ /*&& weight > 1.6*/) {
                attributes.add(new ConceptNode(parent, start.replaceAll("/c/en/","").replaceAll("/n/.*", ""), rel, end.replaceAll("/c/en/","").replaceAll("/n/.*", ""), weight));
            }
        }

        return attributes;
    }

    public static int countAttributes (String concept, int limit, ArrayList<String> edges) throws IOException, JSONException {
        int count = 0;

        JSONObject conceptObj = ConceptQuery.getJSONObject(concept, limit,0);
        JSONArray objArray = conceptObj.getJSONArray("edges");

        for (int i = 0; i<objArray.length(); i++) {
            String start = objArray.getJSONObject(i).get("start").toString();
            String rel = objArray.getJSONObject(i).get("rel").toString();
            String end = objArray.getJSONObject(i).get("end").toString();
            double weight = objArray.getJSONObject(i).getDouble("weight");
            if (edges.contains(rel) && start.equals("/c/en/" + concept) /*&& weight > 1.6*/) {
                //System.out.println("Counting attribute for " + concept + ": " + end);
                count++;
            }
        }

        return count;
    }

    public static Map<String, Integer> countCommonAttrConcept (String[] concepts, String concept) throws IOException, JSONException {
        Map<String, Integer> commonAttributes = new HashMap<>();
        ArrayList<String> edges = ConceptEdges.getEdges();

        Set<ConceptNode> conceptAttrs = new HashSet<>();
        conceptAttrs.addAll(getAttributes(null, concept, edges));

        Map<String, Set<ConceptNode>> ancestorMap = new HashMap<>();
        for (int i = 0; i<concepts.length; i++) {
            ancestorMap.put(concepts[i], new HashSet<>(getAttributes(null, concepts[i], edges)));
            commonAttributes.put(concepts[i], 0);
        }


        // Check for ancestors between two concepts
        for (String ancStr : ancestorMap.keySet()) {
            for (ConceptNode node : conceptAttrs) {
                if (ancestorMap.get(ancStr).contains(node)) {
                    //System.out.println("Found common attribute for " + ancStr + ": " + node.start + " -" + node.rel + "-> " + node.end);
                    commonAttributes.put(ancStr, commonAttributes.get(ancStr) + 1);
                }
            }
        }


        return commonAttributes;
    }
}
