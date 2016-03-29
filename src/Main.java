/**
 * Created by Hoddi on 16.3.2016.
 */

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException, JSONException {

        Collection<Edge> edgeProbabilities = new ArrayList<>();
        edgeProbabilities = ConceptEdges.getProbabilities("mammal");
        System.out.println(edgeProbabilities);

        Collection<Relation> test = new LinkedList<>();
        test = Cougar.findConnections("bird");
        System.out.println(test);

    }
}
