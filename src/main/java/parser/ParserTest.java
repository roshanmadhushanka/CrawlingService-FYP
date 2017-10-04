package parser;

import db.VersionDAO;
import evaluateion.Evaluator;
import model.Version;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by roshanalwis on 10/3/17.
 */
public class ParserTest {
    public static void main(String[] args) {
        // Create versions
        Version versionMedium = new Version();
        versionMedium.setUrl("https://medium.com");

        // Load versions
        VersionDAO versionDAO = new VersionDAO();
        List<Version> versionList = versionDAO.read(versionMedium);

        System.out.println("Number of records : " + versionList.size());

        // Get initial and latest version
        Version oldVersion = versionList.get(0);
        Version newVersion = versionList.get(versionList.size() - 1);

        // Parse web content
        WebPageParser webPageParser = new WebPageParser();
        HashMap<String, ArrayList<ArrayList<String>>> oldExt = webPageParser.extendedList(oldVersion.getContent());
        HashMap<String, ArrayList<ArrayList<String>>> newExt = webPageParser.extendedList(newVersion.getContent());

        HashMap<String, int[]> result = Evaluator.diff(oldExt, newExt);
        for (String key: result.keySet()) {
            System.out.println("Key " + key);
            System.out.println(Arrays.toString(result.get(key)));
        }
    }
}
