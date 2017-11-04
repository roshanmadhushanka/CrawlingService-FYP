package main;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import db.VersionDAO;
import evaluateion.Evaluator;
import model.Url;
import model.User;
import model.Version;
import org.bson.Document;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import parser.WebPageParser;
import request.DifferenceRequest;
import request.VersionRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by roshanalwis on 8/26/17.
 */

@RestController
public class CrawlingServiceController {
    @RequestMapping(value = "/heartBeat", method = RequestMethod.GET)
    public ResponseEntity<String> heartBeat(){
        /*
            Simple heartbeat
         */

        String response = "ACK";

        return new ResponseEntity<String>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/crawl", method = RequestMethod.POST)
    public ResponseEntity<String> crawl(@RequestBody Url url) {
        /*
            Crawl and save to version repository
         */

        String response = null;

        // Setup crawler
        Crawler crawler = Crawler.getCrawler();

        // Read page content
        String content = crawler.crawl(url.toString());

        // Create version
        Version version = new Version(url.toString(), content);

        // Save version
        VersionDAO versionDAO = new VersionDAO();
        versionDAO.create(version);

        response = "Success";

        return new ResponseEntity<String>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/getVersionCount", method = RequestMethod.POST)
    public ResponseEntity<String> getVersionCount(@RequestBody VersionRequest versionRequest){
        /*
            Get summary of version
         */

        String response = null;

        // Create connection to the db
        VersionDAO versionDAO = new VersionDAO();

        // Create transferable object
        Document versionCount = new Document();
        versionCount.put("count", versionDAO.count(versionCount));

        // Convert result into JSON
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            response = objectMapper.writeValueAsString(versionCount);
        } catch (JsonProcessingException e) {

        }

        return new ResponseEntity<String>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/getVersion", method = RequestMethod.POST)
    public ResponseEntity<String> getVersion(@RequestBody VersionRequest versionRequest){
        /*
            Get versions from version repository
         */

        String response = null;

        // Load versions
        VersionDAO versionDAO = new VersionDAO();
        List<Version> versionList = versionDAO.read(versionRequest.toVersion());

        // Convert result into JSON
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            response = objectMapper.writeValueAsString(versionList);
        } catch (JsonProcessingException e) {

        }

        return new ResponseEntity<String>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/getHtmlVersion", method = RequestMethod.POST)
    public ResponseEntity<String> getHtmlVersion(@RequestBody VersionRequest versionRequest) {
        /*
            Get HTML version
         */

        String response = null;

        // Load versions
        VersionDAO versionDAO = new VersionDAO();
        List<Version> versionList = versionDAO.read(versionRequest.toVersion());

        if(versionList.size() > 0) {
            response = versionList.get(0).getContent();
        }

        return new ResponseEntity<String>(response, HttpStatus.OK);
    }


    @RequestMapping(value = "/getTimeStamp", method = RequestMethod.POST)
    public ResponseEntity<String> getVersionMetaData(@RequestBody VersionRequest versionRequest){
        /*
            Get timestamps
         */

        String response = null;

        // Load timestamps
        VersionDAO versionDAO = new VersionDAO();
        List<Document> versionList = versionDAO.readTimeStamp(versionRequest.toVersion());

        // Convert result into JSON
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            response = objectMapper.writeValueAsString(versionList);
        } catch (JsonProcessingException e) {

        }

        return new ResponseEntity<String>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/removeVersion", method = RequestMethod.POST)
    public ResponseEntity<String> removeVersion(@RequestBody VersionRequest versionRequest){
        /*
            Remove version from the version repository
         */

        String response = null;

        VersionDAO versionDAO = new VersionDAO();
        versionDAO.delete(versionRequest.toVersion());
        response = "Success";

        return new ResponseEntity<String>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/getDifference", method = RequestMethod.POST)
    public ResponseEntity<String> getDifference(@RequestBody DifferenceRequest diffRequest){
        /*
            Get difference between two versions of a same web page where version ID's are given
         */

        String response = null;

        // Create version
        Version version = new Version();
        version.setUrl(diffRequest.getUrl());

        // Create version DAO to access the repository
        VersionDAO versionDAO = new VersionDAO();

        // Load versions from the repository
        List<Version> versionList = versionDAO.read(version);

        // Select corresponding versions
        Version oldVersion = versionList.get(diffRequest.getOldVersionId());
        Version newVersion = versionList.get(diffRequest.getNewVersionId());

        // Parsing web content to extended map
        WebPageParser webPageParser = new WebPageParser();
        HashMap<String, ArrayList<ArrayList<String>>> oldExtended = webPageParser.extendedList(oldVersion.getContent());
        HashMap<String, ArrayList<ArrayList<String>>> newExtended = webPageParser.extendedList(newVersion.getContent());

        // Evaluate changes
        HashMap<String, int[]> diffResult = Evaluator.diff(oldExtended, newExtended);

        // Create transferable object
        Document diffTransferableObject = new Document();
        for (String tag: diffResult.keySet()) {
            diffTransferableObject.put(tag, diffResult.get(tag));
        }

        // Convert result into JSON
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            response = objectMapper.writeValueAsString(diffTransferableObject);
        } catch (JsonProcessingException e) {

        }

        return new ResponseEntity<String>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public ResponseEntity<String> test(@RequestBody User user) throws IOException {

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.postForObject("http://localhost:7175/getUser", user, String.class);

        // Result convert to a JSON string
        ObjectMapper mapper = new ObjectMapper();
        JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, User.class);
        List<User> userList = mapper.readValue(response, type);

        return new ResponseEntity<String>(response, HttpStatus.OK);
    }


}
