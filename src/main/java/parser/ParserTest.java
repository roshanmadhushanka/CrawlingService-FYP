package parser;

import db.VersionDAO;
import model.Version;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by roshanalwis on 8/29/17.
 */
public class ParserTest {
    public static void main(String[] args) {
        Version version = new Version();
        version.setUrl("https://medium.com");
        VersionDAO versionDAO = new VersionDAO();
        List<Version> versions = versionDAO.read(version);

        WebPageParser webPageParser = new WebPageParser();
        Document doc = webPageParser.toDocument(versions.get(0).getContent());
        HashMap<String, ArrayList<String>> list =  webPageParser.list(doc);
        for(String key: list.keySet()){
            System.out.println(key);
            System.out.println(Arrays.asList(list.get(key)));
            System.out.println("----------------------------");
        }
    }
}
