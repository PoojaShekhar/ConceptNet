/**
 * Created by Tryggvi on 01/04/2016.
 */
public class ConceptNode {

    public String parent;
    public String start;
    public String rel;
    public String end;

    ConceptNode (String parent, String start, String rel, String end) {
        this.parent = parent;
        this.start = start;
        this.rel = rel;
        this.end = end;
    }

    public boolean equals (ConceptNode node) {
        return node.parent.equals(this.parent)
                && node.start.equals(this.start)
                && node.rel.equals(this.rel)
                && node.end.equals(this.end);
    }

    @Override
    public String toString() {
        return this.parent + ", " + this.start + " --" + this.rel + "-> " + this.end;
    }
}
