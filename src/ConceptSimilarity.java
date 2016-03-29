import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeMap;

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

    public static TreeMap<String, Integer> returnSimilar(String compare, LinkedList<String> compareList) throws IOException, JSONException {

        LinkedList<String> tmpList = new LinkedList<>();
        LinkedList<String> compList = new LinkedList<>();
        HashMap<String, Integer> mapList = new HashMap<>();
        int queryCount = 30;
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
}
