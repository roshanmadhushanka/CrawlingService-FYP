package main;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.eclipse.jetty.util.BlockingArrayQueue;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;

/**
 * Created by roshanalwis on 9/28/17.
 */
public class Crawler {
    public static void main(String[] args) throws IOException {
        java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");


        final WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setPrintContentOnFailingStatusCode(false);
        webClient.getOptions().setTimeout(1000);

        HtmlPage page = webClient.getPage("https://www.medium.com");
        Document document = Jsoup.parse(page.asText());

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


//        String title = doc.select(".r_title").select("h1").text();
//
//        String iFramePath = "http:" + doc.select("#quote_quicktake").select("iframe").attr("src");
//
//        page = webClient.getPage(iFramePath);
//
//        doc = Jsoup.parse(page.asXml());
//
//        System.out.println(title + " | Last Price [$]: " + doc.select("#last-price-value").text());
    }
}
