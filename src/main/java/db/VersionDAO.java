package db;

import com.mongodb.MongoClient;
import com.mongodb.MongoSocketOpenException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import model.Version;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by roshanalwis on 8/26/17.
 */

public class VersionDAO {
    // Logging info
    private final static Logger LOGGER = Logger.getLogger(VersionDAO.class.getName());
    private final static String DB_ERROR = "Cannot connect to the database";
    private final static String ERROR = "Error";

    // Class parameters
    private MongoClient mongoClient;
    private MongoCollection versionCollection;

    public static Document toDocument(Version version){
        Document versionObj = new Document();
        if(version.getUrl() != null){
            versionObj.put("url", version.getUrl());
        }

        if(version.getTimestamp() != null){
            versionObj.put("timestamp", version.getTimestamp());
        }

        if(version.getContent() != null){
            versionObj.put("content", version.getContent());
        }

        return versionObj;
    }

    public VersionDAO() {
        mongoClient = MongoConnector.getMongoClient();
        try {
            versionCollection = mongoClient.getDatabase("ozious").getCollection("version");
        } catch (MongoSocketOpenException e) {
            LOGGER.severe(DB_ERROR);
        } catch (Exception e) {
            LOGGER.severe(ERROR);
        }

    }

    public void create(Version version){
        Document versionObj = toDocument(version);
        try {
            versionCollection.insertOne(versionObj);
        }  catch (MongoSocketOpenException e) {
            LOGGER.severe(DB_ERROR);
        } catch (Exception e) {
            LOGGER.severe(ERROR);
        }
    }

    public List<Version> read(Version version){
        Document versionObject = toDocument(version);

        List<Version> versionList = new ArrayList<>();

        try {
            FindIterable<Document> cursor = versionCollection.find(versionObject);
            for (Document obj: cursor
                    ) {
                Version versionObj = new Version();
                versionObj.setUrl(obj.getString("url"));
                versionObj.setTimestamp(obj.getDate("timestamp"));
                versionObj.setContent(obj.getString("content"));
                versionList.add(versionObj);
            }
        } catch (MongoSocketOpenException e) {
            LOGGER.severe(DB_ERROR);
        } catch (Exception e) {
            LOGGER.severe(ERROR);
        }

        return versionList;
    }

    public List<Document> readTimeStamp(Version version){
        Document versionObject = toDocument(version);

        List<Document> versionList = new ArrayList<>();

        try {
            FindIterable<Document> cursor = versionCollection.find(versionObject);
            for (Document obj: cursor
                    ) {
                Document versionObj = new Document();
                versionObj.put("timestamp", obj.getDate("timestamp"));
                versionList.add(versionObj);
            }
        } catch (MongoSocketOpenException e) {
            LOGGER.severe(DB_ERROR);
        } catch (Exception e) {
            LOGGER.severe(ERROR);
        }

        return versionList;
    }

    public void delete(Version version){
        Document versionObject = toDocument(version);
        try {
            versionCollection.deleteMany(versionObject);
        } catch (MongoSocketOpenException e) {
            LOGGER.severe(DB_ERROR);
        } catch (Exception e) {
            LOGGER.severe(ERROR);
        }
    }

}
