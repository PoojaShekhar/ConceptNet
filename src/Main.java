/**
 * Created by Hoddi on 16.3.2016.
 */

import org.json.JSONObject;
import java.io.IOException;


public class Main {

    public static void main(String[] args) throws IOException {

        /*
         * Query ConceptNet for "animal".
         */
        String animals = ConceptQuery.returnURL("animal",10);
        JSONObject objAnimals = new JSONObject(animals);

        for (int i = 0; i < 10; i++) {
            JSONObject resultsAnimals = objAnimals.getJSONArray("edges").getJSONObject(i);
            String currAnimal = resultsAnimals.getString("start").replaceAll("/c/en/", "");

            /*
             * Query ConceptNet for the current animal from the "animal" query.
             */
            String currAnimalRelations = ConceptQuery.returnURL(currAnimal, 5);
            JSONObject objCurrAnimalRelations = new JSONObject(currAnimalRelations);

            System.out.println("--------------------" + currAnimal + "---------------------------");
            for (int k = 0; k < 5; k++) {
                JSONObject resultsCurrAnimalRelations = objCurrAnimalRelations.getJSONArray("edges").getJSONObject(k);
                System.out.println(resultsCurrAnimalRelations.getString("surfaceText").replaceAll("\\[", "").replaceAll("\\]", ""));
            }
        }
    }
}
