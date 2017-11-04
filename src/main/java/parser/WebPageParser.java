package parser;

import org.eclipse.jetty.util.BlockingArrayQueue;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;
import java.util.StringTokenizer;

/**
 * Created by roshanalwis on 8/29/17.
 */

public class WebPageParser {
    private ArrayList<String> nodes;

    public WebPageParser(){
        this.nodes = new ArrayList<>();
    }

    public Document toDocument(String html){
        return Jsoup.parse(html);
    }

    public HashMap<String, ArrayList<String>> list(Document document){
        // Element map
        HashMap<String, ArrayList<String>> elementMap = new HashMap<>();

        // Breadth first element traversal
        Queue<Element> elementQueue = new BlockingArrayQueue<>();
        elementQueue.add(document.head());
        elementQueue.add(document.body());

        while (!elementQueue.isEmpty()){
            // Pop the current element from the queue
            Element element = elementQueue.remove();

            // Add element to the elementMap
            if(!element.text().isEmpty()){
                if (elementMap.containsKey(element.tagName())) {
                    // Append element text under the existing tag
                    elementMap.get(element.tagName()).add(element.text());
                } else {
                    // Create a new list for the tag and add new element to it
                    ArrayList<String> tagTexts = new ArrayList<>();
                    tagTexts.add(element.text());
                    elementMap.put(element.tagName(), tagTexts);
                }
            }

            // Append children of the current element to the queue
            for(Element e: element.children()){
                elementQueue.add(e);
            }
        }

        return elementMap;
    }

    public HashMap<String, ArrayList<String>> list(String htmlString){
        return list(toDocument(htmlString));
    }

    public HashMap<String, ArrayList<ArrayList<String>>> extendedList(Document document){
        HashMap<String, ArrayList<String>> normalList = list(document);
        return parallelTokenize(normalList);
    }

    public HashMap<String, ArrayList<ArrayList<String>>> extendedList(String htmlString){
        HashMap<String, ArrayList<String>> normalList = list(toDocument(htmlString));
        return parallelTokenize(normalList);
    }

    private HashMap<String, ArrayList<ArrayList<String>>> parallelTokenize(HashMap<String,
            ArrayList<String>> normalList) {
        /*
            Tokenize parallel
         */

        HashMap<String, ArrayList<ArrayList<String>>> extendedList = new HashMap<>();

        for (String key: normalList.keySet()) {
            ArrayList<String> tagContent = normalList.get(key);
            ArrayList<ArrayList<String>> extendedTagContent = new ArrayList<>();

            for(int i=0; i<tagContent.size(); i++){
                extendedTagContent.add(tokenize(tagContent.get(i)));
            }

            extendedList.put(key, extendedTagContent);
        }

        return extendedList;
    }

    private ArrayList<String> tokenize(String text){
        /*
            Tokenize sentence by removing special characters.
         */

        ArrayList<String> wordVec = new ArrayList<>();
        String x = text.replaceAll("[|;:,'\".]", " ");
        StringTokenizer url = new StringTokenizer(x, " ");

        while (url.hasMoreTokens()) {
            wordVec.add(url.nextToken());
        }

        return wordVec;
    }
}
