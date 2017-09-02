package parser;

import org.eclipse.jetty.util.BlockingArrayQueue;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

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
}
