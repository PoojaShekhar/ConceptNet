import org.json.JSONException;

import java.io.IOException;
import java.util.*;

/**
 * Created by Hoddi on 30.3.2016.
 */
public class Final {

    /*
     * showRelations, searches specified levels outwards from a concept trying to find a rel
     * to another concept. If it finds it, it return a collection of Relations.
     */
    public static Collection<Node> showRelations(String concept1, String concept2, boolean searchDown) throws IOException, JSONException{

        List<Node> newColl = new ArrayList<>();
        Collection<Node> tmp = new ArrayList<>();
        Collection<Node> tmpColl = new ArrayList<>();
        Collection<Node> tmpNodes = new ArrayList<>();
        Set<Node> uniqSet = new LinkedHashSet<>();
        List<Node> backtrack = new ArrayList<>();

        boolean found = false;

        //tmp = Cougar.findConnections(concept1);
        tmp = Testing.getNodes(concept1, 50, 100, searchDown);

        ArrayList<String> childrenList = new ArrayList<>();
        ArrayList<String> AllChildrenList = new ArrayList<>();
        childrenList.add(concept1);
        int level = 0;
        stop:
        while (level < 10) {
            level++;
            System.out.println("LEVEL: " + level);
            for (int i = 0; i < tmp.size(); i++) {
                if (searchDown) {
                    Node tmpNode = ((ArrayList<Node>) tmp).get(i);
                    if (tmpNode.child.equals(concept2)) {
                        newColl.add(tmpNode);
                        System.out.println("");
                        System.out.println("Connection Found");
                        System.out.println("");
                        found = true;

                        for (int k = 0; k < newColl.size(); k++) {
                            for (int j = backtrack.size() - 1; j > 0; j--) {
                                if ((newColl).get(k).parent.equals((backtrack).get(j).child)) {
                                    newColl.add((backtrack).get(j));
                                }
                            }
                        }

                        uniqSet.addAll(newColl);
                        newColl.clear();
                        newColl.addAll(uniqSet);


                        for (int j = 0; j < newColl.size(); j++) {
                            System.out.println((newColl).get(j));
                        }
                        break stop;
                    } else {
                        for (int k = 0; k < childrenList.size(); k++) {
                            if (tmpNode.parent.equals(childrenList.get(k))) {
                                tmpColl.add(tmpNode);
                            }
                        }
                    }
                }
                if (!searchDown) {
                    Node tmpNode = ((ArrayList<Node>) tmp).get(i);
                    if (tmpNode.parent.equals(concept2)) {
                        newColl.add(tmpNode);
                        System.out.println("");
                        System.out.println("Connection Found");
                        System.out.println("");
                        found = true;

                        for (int k = 0; k < newColl.size(); k++) {
                            for (int j = backtrack.size() - 1; j > 0; j--) {
                                if ((newColl).get(k).child.equals((backtrack).get(j).parent)) {
                                    newColl.add((backtrack).get(j));
                                }
                            }
                        }

                        uniqSet.addAll(newColl);
                        newColl.clear();
                        newColl.addAll(uniqSet);


                        for (int j = 0; j < newColl.size(); j++) {
                            System.out.println((newColl).get(j));
                        }
                        break stop;
                    } else {
                        for (int k = 0; k < childrenList.size(); k++) {
                            if (tmpNode.child.equals(childrenList.get(k))) {
                                tmpColl.add(tmpNode);
                            }
                        }
                    }
                }
            }
            System.out.println("");
            System.out.println(level + ". Iteration Leftovers");
            System.out.println("");
            for (int i = 0; i < tmpColl.size(); i++) {
                System.out.println(((ArrayList<Node>)tmpColl).get(i));
            }
            //expand
            tmp.clear();
            AllChildrenList.addAll(childrenList);
            childrenList.clear();
            if (searchDown) {
                for (int i = 0; i < tmpColl.size(); i++) {
                    if (!childrenList.contains(((ArrayList<Node>) tmpColl).get(i).child) && ((ArrayList<Node>) tmpColl).get(i).weight > 2.5) {
                        childrenList.add(((ArrayList<Node>) tmpColl).get(i).child);
                        for (int j = 0; j < childrenList.size(); j++) {
                            if (AllChildrenList.contains(childrenList.get(j))) {
                                childrenList.remove(childrenList.get(j));
                            }
                        }
                    }
                }
                for (int i = 0; i < tmpColl.size(); i++) {
                    if (childrenList.contains(((ArrayList<Node>) tmpColl).get(i).child)) {
                        backtrack.add(((ArrayList<Node>) tmpColl).get(i));
                    }
                }
            }
            if (!searchDown) {
                for (int i = 0; i < tmpColl.size(); i++) {
                    if (!childrenList.contains(((ArrayList<Node>) tmpColl).get(i).parent) && ((ArrayList<Node>) tmpColl).get(i).weight > 2.5) {
                        childrenList.add(((ArrayList<Node>) tmpColl).get(i).parent);
                        for (int j = 0; j < childrenList.size(); j++) {
                            if (AllChildrenList.contains(childrenList.get(j))) {
                                childrenList.remove(childrenList.get(j));
                            }
                        }
                    }
                }
                for (int i = 0; i < tmpColl.size(); i++) {
                    if (childrenList.contains(((ArrayList<Node>) tmpColl).get(i).parent)) {
                        backtrack.add(((ArrayList<Node>) tmpColl).get(i));
                    }
                }
            }
            System.out.println("");
            System.out.println("Backtrack level: " + level);
            for (int i = 0; i < backtrack.size(); i++) {
                System.out.println((backtrack).get(i));
            }
            System.out.println("");


            System.out.println("");
            System.out.println("Children to search next:");
            System.out.println(childrenList);
            System.out.println("");
            tmpColl.clear();

            for (int i = 0; i < childrenList.size(); i++) {
                //tmpNodes = Cougar.findConnections((childrenList).get(i));
                tmpNodes = Testing.getNodes((childrenList).get(i),50,100,searchDown);
                tmp.addAll(tmpNodes);
            }

            for (int i = 0; i < tmp.size(); i++) {
                System.out.println(((ArrayList<Node>)tmp).get(i));
            }
            System.out.println("");

        }
        if (!found) {
            System.out.println("No Path Found for Depth: " + level);
        }
        return newColl;
    }
}
