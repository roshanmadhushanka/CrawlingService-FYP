package request;

import model.Version;

import java.sql.Timestamp;

/**
 * Created by roshanalwis on 9/26/17.
 */
public class VersionRequest {
    private String url;
    private Timestamp timestamp;

    public VersionRequest() {
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

    public Version toVersion(){
        Version version = new Version();
        version.setUrl(url);
        version.setTimestamp(timestamp);
        return version;
    }
}
