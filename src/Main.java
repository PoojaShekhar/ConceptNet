/**
 * Created by Hoddi on 16.3.2016.
 */

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.LinkedList;

public class Main {

    public static void main(String[] args) throws IOException, JSONException {


        int animalQuery = 20;
        int sheepQuery = 30;
        int goatQuery = 30;

        LinkedList<String> Animals = new LinkedList<>();
        LinkedList<String> AnimalsUnique = new LinkedList<>();

        LinkedList<String> Sheep = new LinkedList<>();
        LinkedList<String> Goat = new LinkedList<>();
        LinkedList<String> SameList = new LinkedList<>();
        LinkedList<String> DiffList = new LinkedList<>();
        /*
         * Query ConceptNet for "animal".
         */
        String animals = ConceptQuery.returnURL("animal",animalQuery);
        JSONObject objAnimals = new JSONObject(animals);

        for (int i = 0; i < animalQuery; i++) {
            JSONObject resultsAnimals = objAnimals.getJSONArray("edges").getJSONObject(i);
            String currAnimal = resultsAnimals.getString("start").replaceAll("/c/en/", "");
            Animals.add(currAnimal);
        }

        for (int i = 0; i < Animals.size(); i++)  {
            if (!AnimalsUnique.contains(Animals.get(i))) {
                AnimalsUnique.add(Animals.get(i));
            }
        }

        String sheepQ = ConceptQuery.returnURL("cougar", sheepQuery);
        JSONObject jsonSheep = new JSONObject(sheepQ);

        for (int i = 0; i < sheepQuery; i++) {
            JSONObject resSheep = jsonSheep.getJSONArray("edges").getJSONObject(i);
            String firstSheep = resSheep.getString("end").replaceAll("/c/en/", "");
            Sheep.add(firstSheep);
        }

        String goatQ = ConceptQuery.returnURL("mountain_lion", goatQuery);
        JSONObject jsonGoat = new JSONObject(goatQ);

        for (int i = 0; i < goatQuery; i++) {
            JSONObject resGoat = jsonGoat.getJSONArray("edges").getJSONObject(i);
            String firstGoat = resGoat.getString("end").replaceAll("/c/en/","");
            Goat.add(firstGoat);
        }

        System.out.println("------------SHEEP----------------");
        for (int i = 0; i < Sheep.size(); i++) {
            System.out.println(Sheep.get(i));
        }

        System.out.println("-------------GOAT---------------");
        for (int i = 0; i < Goat.size(); i++) {
            System.out.println(Goat.get(i));
        }

        for (int i = 0; i < Goat.size(); i++) {
            for (int k = 0; k < Sheep.size(); k++) {
                if (Goat.get(i).equals(Sheep.get(k))) {
                    SameList.add(Goat.get(i));
                }
            }
        }

        System.out.println("----------SIMILARITIES-------------");
        for (int i = 0; i < SameList.size(); i++) {
            System.out.println(SameList.get(i));
        }
    }
}
