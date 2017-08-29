package model;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by roshanalwis on 8/26/17.
 */
public class Version {
    private String url;
    private Timestamp timestamp;
    private String content;

    public Version() {

    }

    public Version(String url, String content){
        this.url = url;
        this.content = content;
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public void setTimestamp(String timestampString) {
        this.timestamp = Timestamp.valueOf(timestampString);
    }

    public void setTimestamp(Date date) {
        this.timestamp = new Timestamp(date.getTime());
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void display(){
        System.out.println("URL       : " + url);
        System.out.println("Timestamp : " + timestamp);
        System.out.println("Content   : ");
        System.out.println(content);
    }

}
