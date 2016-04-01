import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by HörðurMár on 1.4.2016.
 */
public class ConceptScore {

    public static String getJSON(String concept, int limit) throws IOException {

        String s = "http://conceptnet5.media.mit.edu/data/5.4/assoc/list/en/" + concept + "?limit=" + limit + "&filter=/c/en/";
        URL url = new URL(s);

        /*
         * Read the URL
         */
        Scanner scan = new Scanner(url.openStream());
        String str = new String();
        while (scan.hasNext())
            str += scan.nextLine();
        scan.close();

        return str;
    }

    public static ArrayList<String> getTerms(String str, int terms) throws IOException, JSONException {

        ArrayList<String> list = new ArrayList<>();
        str = getJSON(str, terms);

        JSONObject obj = new JSONObject(str);
        JSONArray array = obj.getJSONArray("similar");
        for (int i = 0; i < array.length(); i++) {
            String con = ((String) array.getJSONArray(i).get(0)).replaceAll("/c/en/","");
            list.add(con);
        }
        return list;
    }

    public static Map<String, Integer> getTermsScore(String termOne, String termTwo, int terms) throws IOException, JSONException {

        Map<String, Integer> termMap = new HashMap<String, Integer>();

        ArrayList<String> term1 = getTerms(termOne, terms);
        ArrayList<String> term2 = getTerms(termTwo, terms);
        ArrayList<String> tmpUniq = new ArrayList<>();

        for (int i = 0; i < term1.size(); i++) {
            for (int k = 0; k < term2.size(); k++) {
                if (term1.contains(term2.get(k)) && !tmpUniq.contains(term2.get(k))) {
                    tmpUniq.add(term2.get(k));
                }
            }
        }
        termMap.put(termTwo, tmpUniq.size());

        return termMap;
    }

    public static Map<String, Integer> getTermsScore(String termOne, ArrayList<String> concepts, int limit) throws  JSONException, IOException {

        Map<String, Integer> map = new HashMap<>();
        Map<String, Integer> tmpMap = new HashMap<>();

        for (int i = 0; i < concepts.size(); i++) {
            tmpMap = getTermsScore(termOne, concepts.get(i), limit);
            for (Map.Entry<String, Integer> entry : tmpMap.entrySet()) {
                map.put(entry.getKey(), entry.getValue());
                tmpMap.clear();
            }
        }
        Map<String, Integer> sortedMap = ConceptSimilarity.sortMapByValue((HashMap<String, Integer>) map);
        return sortedMap;
    }
}
