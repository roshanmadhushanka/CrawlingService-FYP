package model;

import db.VersionDAO;
import evaluateion.Evaluator;
import org.bson.Document;
import parser.WebPageParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by roshanalwis on 9/26/17.
 */
public class Difference {
    private int oldVersionId;
    private int newVersionId;
    private String url;

    public Difference() {
        oldVersionId = 0;
        newVersionId = -1;
    }

    public Difference(int oldVersionId, int newVersionId, String url) {
        this.oldVersionId = oldVersionId;
        this.newVersionId = newVersionId;
        this.url = url;
    }

    public int getOldVersionId() {
        return oldVersionId;
    }

    public void setOldVersionId(int oldVersionId) {
        this.oldVersionId = oldVersionId;
    }

    public int getNewVersionId() {
        return newVersionId;
    }

    public void setNewVersionId(int newVersionId) {
        this.newVersionId = newVersionId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Document calculateDifference(){
        // Create version query
        Version version = new Version();
        version.setUrl(url);

        // Load versions
        VersionDAO versionDAO = new VersionDAO();
        List<Version> versions = versionDAO.read(version);

        // Result
        Document diffResult = new Document();

        // Select versions
        if(versions.size() >= 2) {
            if(oldVersionId < 0){
                oldVersionId = versions.size() + oldVersionId;
            }

            if(newVersionId < 0){
                newVersionId = versions.size() + newVersionId;
            }

            WebPageParser webPageParser = new WebPageParser();
            HashMap<String, ArrayList<ArrayList<String>>> oldVersion = webPageParser.extendedList(versions.get(oldVersionId).getContent());
            HashMap<String, ArrayList<ArrayList<String>>> newVersion = webPageParser.extendedList(versions.get(newVersionId).getContent());

            // Measure difference
            HashMap<String, int[]> diffInfo = Evaluator.diff(oldVersion, newVersion);

            // Generate transferable object
            for(String key: diffInfo.keySet()){
                diffResult.put(key, diffInfo.get(key));
            }
        }

        return diffResult;
    }
}
