import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Hoddi on 30.3.2016.
 */
public class Final {

    /*
     * showRelations, searches 3 level outwards from a concept trying to find a relation
     * to another concept. If it finds it, it return a collection of Relations.
     */
    public static Collection<Relation> showRelations(String concept1, String concept2) throws IOException, JSONException{

        Collection<Relation> newRel = new ArrayList<>();
        Collection<Relation> tmp = new ArrayList<>();
        Collection<Relation> tmp2 = new ArrayList<>();
        Collection<Relation> tmp3 = new ArrayList<>();

        tmp = Cougar.findConnections(concept1);
        for (int i = 0; i < tmp.size(); i++) {
            Relation tmpRel = ((ArrayList<Relation>)tmp).get(i);
            if (tmpRel.concept.contains(concept2)) {
                newRel.add(tmpRel);
            }
            else {
                tmp2 = Cougar.findConnections(tmpRel.concept);
                for (int k = 0; k < tmp2.size(); k++) {
                    Relation tmp2Rel = ((ArrayList<Relation>)tmp2).get(k);
                    if (tmp2Rel.concept.contains(concept2)) {
                        newRel.add(tmpRel);
                        newRel.add(tmp2Rel);
                    }
                    else {
                        tmp3 = Cougar.findConnections(tmp2Rel.concept);
                        for (int j = 0; j < tmp3.size(); j++) {
                            Relation tmp3Rel = ((ArrayList<Relation>)tmp3).get(j);
                            if (tmp3Rel.concept.contains(concept2)) {
                                newRel.add(tmpRel);
                                newRel.add(tmp2Rel);
                                newRel.add(tmp3Rel);
                            }
                        }
                    }
                }
            }

        }

        return newRel;
    }
}
