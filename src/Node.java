/**
 * Created by Hoddi on 3.4.2016.
 */
public class Node {

    String parent;
    String child;
    String relation;
    double weight;

    public Node() {

    }

    public Node (String parent, String child, String relation, double weight) {
        this.parent = parent;
        this.child = child;
        this.relation = relation;
        this.weight = weight;
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

        hash = hash * prime + this.parent.hashCode();
        hash = hash * prime + this.child.hashCode();
        hash = hash * prime + this.relation.hashCode();
        hash = hash * prime + (int)this.weight;

        return hash;
    }

    public String toString() {
        return parent +  " " + relation + " " + child + " " + weight;
    }
}
