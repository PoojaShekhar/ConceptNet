/**
 * Created by Hoddi on 17.3.2016.
 */
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class ConceptQuery {

    /*
     * limit specifies the length of our ConceptNet5 query.
     * concept is the concept queried.
     */
    public static String returnURL(String concept, int limit) throws IOException {

        String s = "http://conceptnet5.media.mit.edu/data/5.3/c/en/" + concept + "?limit=" + limit;
        URL url = new URL(s);

        /*
         * Read the URL
         */
        Scanner scan = new Scanner(url.openStream());
        String str = new String();
        while (scan.hasNext())
            str += scan.nextLine();
        scan.close();

        return str;
    }
}
