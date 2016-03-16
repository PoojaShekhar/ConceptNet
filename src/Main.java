/**
 * Created by Hoddi on 16.3.2016.
 */

import org.json.JSONObject;

import java.net.URL;
import java.util.Scanner;
import java.io.IOException;


public class Main {

    public static void main(String[] args) throws IOException {

        /*
         * limit specifies the length of our ConceptNet5 query.
         */
        String limit = "10";
        String queryWhat = "animal";
        int limitToInt = Integer.valueOf(limit);

        String s = "http://conceptnet5.media.mit.edu/data/5.3/c/en/" + queryWhat + "?limit=" + limit;
        URL url = new URL(s);

        /*
         * Read the URL
         */
        Scanner scan = new Scanner(url.openStream());
        String str = new String();
        while (scan.hasNext())
            str += scan.nextLine();
        scan.close();

        /*
         * Create the JSON object using org.json library
         */
        JSONObject obj = new JSONObject(str);

        if (! obj.getString("numFound").equals("OK"))
            return;

        for (int i = 0; i < limitToInt; i++) {
            JSONObject results = obj.getJSONArray("edges").getJSONObject(i);
            System.out.println(results.getString("start").replaceAll("/c/en/", ""));
        }
    }
}
