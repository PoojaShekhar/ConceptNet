/**
 * Created by Hoddi on 16.3.2016.
 */

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
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

        boolean conceptRelations = true;
        boolean conceptSimilarity = false;
        boolean conceptMatches = false;
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
        LinkedList<String> conceptList = new LinkedList<>();
        TreeMap<String, Integer> comparedList = new TreeMap<>();

        ///////////////////////////////////////////////////////////////////////
        /////////////////////////// Simple ConceptNet5 Query //////////////////
        ///////////////////////////////////////////////////////////////////////
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
        ////////////////////////////////////////////////////////////////////
        /////////////// Counting matches between concepts //////////////////
        ////////////////////////////////////////////////////////////////////
        if (conceptMatches) {
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
                String firstGoat = resGoat.getString("end").replaceAll("/c/en/", "");
                Goat.add(firstGoat);
            }
            Goat = makeUnique(Goat);

            System.out.println("------------" + concept1.toUpperCase() + "----------------");
            for (int i = 0; i < Sheep.size(); i++) {
                System.out.println(Sheep.get(i));
            }
            System.out.println();

            System.out.println("-------------" + concept2.toUpperCase() + "---------------");
            for (int i = 0; i < Goat.size(); i++) {
                System.out.println(Goat.get(i));
            }
            System.out.println();
        /*
         * Similarities between concept1 & concept2.
         */
            for (int i = 0; i < Goat.size(); i++) {
                for (int k = 0; k < Sheep.size(); k++) {
                    if (Goat.get(i).equals(Sheep.get(k))) {
                        SameList.add(Goat.get(i));
                    }
                }
            }
        /*
         * Counting similarities between a concept and a list of other concepts.
         */
            System.out.println("----------SIMILARITIES: " + concept1.toUpperCase() + " & " + concept2.toUpperCase() + "-------------");
            for (int i = 0; i < SameList.size(); i++) {
                System.out.println(SameList.get(i));
            }
            System.out.println();
        }

        ///////////////////////////////////////////////////////////////////////
        ///////////////////// ConceptSimilarity ///////////////////////////////
        ///////////////////////////////////////////////////////////////////////
        if (conceptSimilarity) {
            String conceptItem = "dog";
            System.out.println("---------LIST OF MOST SIMILAR FOR: " + conceptItem.toUpperCase() + "------------------");
            conceptList.add("mouse");
            conceptList.add("bird");
            conceptList.add("cat");
            conceptList.add("puma");
            conceptList.add("spoon");
            conceptList.add("cougar"); // fyrir Tryggva.
            conceptList.add("car");
            conceptList.add("chicken");
            conceptList.add("squirrel");
            comparedList = ConceptSimilarity.returnSimilar(conceptItem, conceptList);
            System.out.println(comparedList);
            System.out.println();
        }

        ////////////////////////////////////////////////////////////////////////
        /////////////////////// ConceptRelations ///////////////////////////////
        ////////////////////////////////////////////////////////////////////////
        if (conceptRelations) {
            String conceptChildren = "machine";
            System.out.println("-----------FIND CHILDREN OF: " + conceptChildren.toUpperCase() + "---------");
            LinkedList<String> listChildren = ConceptRelations.findChildren(conceptChildren);
            for (int i = 0; i < listChildren.size(); i++) {
                System.out.println(listChildren.get(i));
            }
            System.out.println();

            String conceptParent = "mammal";
            System.out.println("-----------FIND PARENT OF: " + conceptParent.toUpperCase() + "---------");
            LinkedList<String> listParent = ConceptRelations.findParents(conceptParent);
            for (int i = 0; i < listParent.size(); i++) {
                System.out.println(listParent.get(i));
            }
            System.out.println();
        }
    }
}
