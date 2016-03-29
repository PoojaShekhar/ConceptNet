import sun.plugin2.message.ReleaseRemoteObjectMessage;

/**
 * Created by Hoddi on 29.3.2016.
 */
public class Relation {

    String relation;
    String concept;

    public Relation(String relation, String concept) {

        this.relation = relation;
        this.concept = concept;
    }

    @Override
    public String toString()
    {
        return (relation + " " + concept + "\n");
    }

    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        return this.hashCode() == obj.hashCode();
    }

    public int hashCode() {

        int hash = 1;
        int prime = 113;

        hash = hash * prime + relation.hashCode();
        hash = hash * prime + concept.hashCode();

        return hash;
    }
}
