package request;

/**
 * Created by roshanalwis on 9/26/17.
 */

public class DifferenceRequest {
    private int oldVersionId;
    private int newVersionId;
    private String url;

    public DifferenceRequest() {
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
}
