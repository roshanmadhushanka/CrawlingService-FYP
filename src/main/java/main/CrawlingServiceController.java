package main;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import db.VersionDAO;
import model.Url;
import model.User;
import model.Version;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

/**
 * Created by roshanalwis on 8/26/17.
 */

@RestController
public class CrawlingServiceController {
    @RequestMapping(value = "/crawl", method = RequestMethod.POST)
    public ResponseEntity<Version> crawl(@RequestBody Url url){
        HtmlUnitDriver driver = new HtmlUnitDriver(BrowserVersion.FIREFOX_45);

        // Allow to load javascripts
        driver.setJavascriptEnabled(true);

        // Navigate to URL
        driver.get(url.toString());

        // Read page content
        String content = driver.getPageSource();

        // Close driver
        driver.quit();

        // Create version
        Version version = new Version(url.toString(), content);

        // Save version
        VersionDAO versionDAO = new VersionDAO();
        versionDAO.create(version);

        return new ResponseEntity<Version>(version, HttpStatus.OK);
    }

    @RequestMapping(value = "/getVersions", method = RequestMethod.POST)
    public ResponseEntity<List<Version>> getVersion(@RequestBody Version version){
        VersionDAO versionDAO = new VersionDAO();
        List<Version> versionList = versionDAO.read(version);
        return new ResponseEntity<List<Version>>(versionList, HttpStatus.OK);
    }

    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public ResponseEntity<String> test(@RequestBody User user) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.postForObject("http://localhost:7175/getUser", user, String.class);

        ObjectMapper mapper = new ObjectMapper();
        JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, User.class);
        List<User> userList = mapper.readValue(response, type);
        System.out.println(userList.toString());

        return new ResponseEntity<String>(response, HttpStatus.OK);
    }


}
