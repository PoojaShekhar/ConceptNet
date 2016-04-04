/**
 * Created by Tryggvi on 01/04/2016.
 */
public class ConceptNode {

    public ConceptNode parent;
    public String start;
    public String rel;
    public String end;
    public double weight;


    //ConceptNode (null, "sheep", "/r/IsA", "mammal", 2.32)
    ConceptNode (ConceptNode parent, String start, String rel, String end, double weight) {
        this.parent = parent;
        this.start = start;
        this.rel = rel;
        this.end = end;
        this.weight = weight;
    }

    @Override
    public String toString () {
        return this.start + " -" + this.rel + "-> " + this.end + ": " + this.weight;
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

//        if (this.parent == null) hash = hash * prime + prime;
//        else hash = hash * prime + this.parent.start.hashCode();
//        hash = hash * prime + this.start.hashCode();
        hash = hash * prime + this.rel.hashCode();
        hash = hash * prime + this.end.hashCode();
//        hash = hash * prime + (int)this.weight;

        return hash;
    }
}
