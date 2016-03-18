/**
 * Created by Hoddi on 16.3.2016.
 */

import org.json.JSONObject;

import java.io.IOException;


public class Main {

    public static void main(String[] args) throws IOException {

        ConceptQuery conceptQuery = new ConceptQuery();

        String animals = conceptQuery.returnURL("animal",10);
        JSONObject obj = new JSONObject(animals);

        for (int i = 0; i < 10; i++) {
            JSONObject results = obj.getJSONArray("edges").getJSONObject(i);
            String currAnimal = results.getString("start").replaceAll("/c/en/", "");
            //System.out.println(results.getString("start").replaceAll("/c/en/", ""));

            String animalInfo = conceptQuery.returnURL(currAnimal, 5);
            JSONObject obj2 = new JSONObject(animalInfo);

            System.out.println("--------------------" + currAnimal + "---------------------------");
            for (int k = 0; k < 5; k++) {
                JSONObject results2 = obj2.getJSONArray("edges").getJSONObject(k);
                System.out.println(results2.getString("surfaceText").replaceAll("\\[", "").replaceAll("\\]", ""));
            }
        }
    }
}
