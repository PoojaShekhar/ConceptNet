/**
 * Created by Hoddi on 16.3.2016.
 */

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

public class Main {

    public static Collection<Relation> putProb(Collection<Relation> rel, Collection<Edge> prob) {

        Collection<Relation> newRel = new ArrayList<>();
        for (int i = 0; i < rel.size(); i++) {
            Relation tmpRel = ((ArrayList<Relation>) rel).get(i);
            for (int k = 0; k < prob.size(); k++) {
                Edge tmpEdge = ((ArrayList<Edge>)prob).get(k);
                if (tmpRel.relation.contains(tmpEdge.name.replaceAll("- /r/", ""))) {
                    tmpRel.prob = tmpEdge.value;
                    newRel.add(tmpRel);
                }
            }
        }
        return newRel;
    }

    public static void main(String[] args) throws IOException, JSONException {

        Collection<Edge> edgeProbabilities = new ArrayList<>();
        edgeProbabilities = ConceptEdges.getProbabilities("insect");
        System.out.println(edgeProbabilities);

        Collection<Relation> test = new ArrayList<>();
        test = Cougar.findConnections("tire");
        System.out.println(test);

        Collection<Relation> try1 = new ArrayList<>();
        try1 = putProb(test,edgeProbabilities);
        System.out.println(try1);

        Collection<Relation> test3 = new ArrayList<>();
        test3 = Final.showRelations("mammal", "find_mate");
        System.out.println(test3);

    }
}
