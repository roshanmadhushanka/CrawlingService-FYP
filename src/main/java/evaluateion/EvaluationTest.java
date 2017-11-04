package evaluateion;

import db.VersionDAO;
import model.Version;
import java.util.List;

public class EvaluationTest {
    public static void main(String[] args) {
        VersionDAO versionDAO = new VersionDAO();
        Version version = new Version();
        version.setUrl("http://edition.cnn.com");
        List<Version> versionList = versionDAO.read(version);

        Version oldVersion = versionList.get(0);
        for(int i=0; i<versionList.size(); i++) {
            Version newVersion = versionList.get(i);
            ChangeDetector changeDetector = new ChangeDetector(oldVersion.getContent(), newVersion.getContent());
            System.out.println(changeDetector.tagDiff("div"));
        }

    }
}
