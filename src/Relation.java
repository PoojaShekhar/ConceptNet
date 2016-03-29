/**
 * Created by HörðurMár on 29.3.2016.
 */
public class Relation {

    String relation;
    String concept;

    public Relation(String relation, String concept) {

        this.relation = relation;
        this.concept = concept;
    }

    public int hashCode() {

        int hash = 1;
        int prime = 6029;

        hash = hash * prime + relation.hashCode();
        hash = hash * prime + concept.hashCode();
        return hash;
    }

    @Override
    public String toString() {
        return (relation + " " + concept + "\n");
    }
}
