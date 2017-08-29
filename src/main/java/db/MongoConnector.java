package db;

import com.mongodb.MongoClient;

/**
 * Created by roshanalwis on 8/26/17.
 */
public class MongoConnector {
    private static MongoClient mongoClient = null;

    public static MongoClient getMongoClient(){
        if(mongoClient == null){
            mongoClient = new MongoClient();
        }
        return mongoClient;
    }
}
