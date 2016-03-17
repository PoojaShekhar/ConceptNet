/**
 * Created by Hoddi on 16.3.2016.
 */

import org.json.JSONObject;

import java.io.IOException;


public class Main {

    public static void main(String[] args) throws IOException {

        ConceptQuery conceptQuery = new ConceptQuery();
        String animals = conceptQuery.returnURL("animal",5);
        JSONObject obj = new JSONObject(animals);

        for (int i = 0; i < 5; i++) {
            JSONObject results = obj.getJSONArray("edges").getJSONObject(i);
            System.out.println(results.getString("start").replaceAll("/c/en/", ""));
        }
    }
}
