package db;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import model.Version;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by roshanalwis on 8/26/17.
 */

public class VersionDAO {
    private MongoClient mongoClient;
    private MongoCollection userCollection;

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
        userCollection = mongoClient.getDatabase("ozious").getCollection("version");
    }

    public void create(Version version){
        Document versionObj = toDocument(version);
        userCollection.insertOne(versionObj);
    }

    public List<Version> read(Version version){
        Document versionObject = toDocument(version);

        List<Version> versionList = new ArrayList<>();
        FindIterable<Document> cursor = userCollection.find(versionObject);
        for (Document obj: cursor
                ) {
            Version versionObj = new Version();
            versionObj.setUrl(obj.getString("url"));
            versionObj.setTimestamp(obj.getDate("timestamp"));
            versionObj.setContent(obj.getString("content"));
            versionList.add(versionObj);
        }
        return versionList;
    }

    public void delete(Version version){

    }

}
