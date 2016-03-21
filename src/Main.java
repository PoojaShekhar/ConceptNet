/**
 * Created by Hoddi on 16.3.2016.
 */

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.TreeMap;

public class Main {

    /*
     * Make the LinkedList<String> someList unique.
     */
    public static LinkedList<String> makeUnique(LinkedList<String> theList) {
        LinkedList<String> uniqueList = new LinkedList<>();
        for (int i = 0; i < theList.size(); i++)  {
            if (!uniqueList.contains(theList.get(i))) {
                uniqueList.add(theList.get(i));
            }
        }
        return uniqueList;
    }

    public static void main(String[] args) throws IOException, JSONException {


        int animalQuery = 20;
        int sheepQuery = 30;
        int goatQuery = 30;
        String concept1 = "dog";
        String concept2 = "cat";

        LinkedList<String> Animals = new LinkedList<>();
        LinkedList<String> AnimalsUnique = new LinkedList<>();

        LinkedList<String> Sheep = new LinkedList<>();
        LinkedList<String> Goat = new LinkedList<>();
        LinkedList<String> SameList = new LinkedList<>();
        Collection<String> queryList = new ArrayList<>();
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
        /*
         * Query concept1.
         */
        String sheepQ = ConceptQuery.returnURL(concept1, sheepQuery);
        JSONObject jsonSheep = new JSONObject(sheepQ);

        for (int i = 0; i < sheepQuery; i++) {
            JSONObject resSheep = jsonSheep.getJSONArray("edges").getJSONObject(i);
            String firstSheep = resSheep.getString("end").replaceAll("/c/en/", "");
            Sheep.add(firstSheep);
        }
        Sheep = makeUnique(Sheep);
        /*
         * Query concept2.
         */
        String goatQ = ConceptQuery.returnURL(concept2, goatQuery);
        JSONObject jsonGoat = new JSONObject(goatQ);

        for (int i = 0; i < goatQuery; i++) {
            JSONObject resGoat = jsonGoat.getJSONArray("edges").getJSONObject(i);
            String firstGoat = resGoat.getString("end").replaceAll("/c/en/","");
            Goat.add(firstGoat);
        }
        Goat = makeUnique(Goat);

        System.out.println("------------" + concept1 + "----------------");
        for (int i = 0; i < Sheep.size(); i++) {
            System.out.println(Sheep.get(i));
        }

        System.out.println("-------------" + concept2 + "---------------");
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
        /*
         * Checking for similarities between a concept and a list of other concepts.
         */
        System.out.println("----------SIMILARITIES: " + concept1 + " & " + concept2 + "-------------");
        for (int i = 0; i < SameList.size(); i++) {
            System.out.println(SameList.get(i));
        }

        String conceptItem = "dog";
        System.out.println("---------LIST OF MOST SIMILAR FOR: " + conceptItem + "------------------");
        LinkedList<String> conceptList = new LinkedList<>();
        TreeMap<String, Integer> comparedList = new TreeMap<>();
        conceptList.add("mouse");
        conceptList.add("bird");
        conceptList.add("cat");
        conceptList.add("puma");
        conceptList.add("spoon");
        conceptList.add("cougar"); // fyrir Tryggva.
        conceptList.add("car");
        conceptList.add("chicken");
        conceptList.add("squirrel");
        comparedList = Similarity.returnSimilar(conceptItem, conceptList);
        System.out.println(comparedList);

    }
}
