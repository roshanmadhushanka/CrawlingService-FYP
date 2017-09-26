package parser;

import db.VersionDAO;
import evaluateion.Evaluator;
import model.Version;

import java.util.ArrayList;
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

//        WebPageParser webPageParser = new WebPageParser();
//        Document doc = webPageParser.toDocument(versions.get(0).getContent());
//        HashMap<String, ArrayList<String>> list =  webPageParser.list(doc);
//        for(String key: list.keySet()){
//            System.out.println(key);
//            System.out.println(Arrays.asList(list.get(key)));
//            System.out.println("----------------------------");
//        }

        WebPageParser webPageParser = new WebPageParser();
        HashMap<String, ArrayList<ArrayList<String>>> version1 = webPageParser.extendedList(versions.get(0).getContent());
        HashMap<String, ArrayList<ArrayList<String>>> version2 = webPageParser.extendedList(versions.get(3).getContent());
        Evaluator.diff(version1, version2);
        System.out.println("Done");


    }


}
