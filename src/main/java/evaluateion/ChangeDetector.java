package evaluateion;

import javafx.util.Pair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.util.ArrayList;
import java.util.List;

public class ChangeDetector {
    private Document oldVersion;
    private Document newVersion;

    public ChangeDetector(String oldVersionHtml, String newVersionHtml) {
        this.oldVersion = Jsoup.parse(oldVersionHtml);
        this.newVersion = Jsoup.parse(newVersionHtml);
    }

    public org.bson.Document aDiff() {
        List<Element> oldAnchors = oldVersion.select("a");
        List<Element> newAnchors = newVersion.select("a");

        List<Pair> oldAnchorTextPair = new ArrayList<>();
        List<Pair> newAnchorTextPair = new ArrayList<>();

        for(Element el: oldAnchors) {
            oldAnchorTextPair.add(new Pair(el.text(), el.attr("href")));
        }

        for(Element el: newAnchors) {
            newAnchorTextPair.add(new Pair(el.text(), el.attr("href")));
        }

        int unchangedCount = 0; // No of links that have remained same
        int deletedCount = 0;   // No of links deleted
        int newCount = 0;       // Newly added links

        // Compute difference
        for(Pair p: oldAnchorTextPair) {
            if(newAnchorTextPair.contains(p)) {
                unchangedCount++;
            } else {
                deletedCount++;
            }
        }

        for(Pair p: newAnchorTextPair) {
            if(!oldAnchorTextPair.contains(p)) {
                newCount++;
            }
        }

        // Generate transferable object
        org.bson.Document anchorDiffInfo = new org.bson.Document();
        anchorDiffInfo.put("new", newCount);
        anchorDiffInfo.put("delete", deletedCount);
        anchorDiffInfo.put("same", unchangedCount);

        return  anchorDiffInfo;
    }

    public org.bson.Document tagDiff(String tagName) {
        /*
            Measure tag evolution
         */
        List<Element> oldTags = oldVersion.select(tagName);
        List<Element> newTags = newVersion.select(tagName);

        List<String> oldTagText = new ArrayList<>();
        List<String> newTagText = new ArrayList<>();

        for(Element el: oldTags) {
            oldTagText.add(el.text());
        }

        for(Element el: newTags) {
            newTagText.add(el.text());
        }

        int unchangedCount = 0; // No of links that have remained same
        int deletedCount = 0;   // No of links deleted
        int newCount = 0;       // Newly added links

        // Compute difference
        for(String s: oldTagText) {
            if(newTagText.contains(s)) {
                unchangedCount++;
            } else {
                deletedCount++;
            }
        }

        for(String s: newTagText) {
            if(!oldTagText.contains(s)) {
                newCount++;
            }
        }

        // Generate transferable object
        org.bson.Document anchorDiffInfo = new org.bson.Document();
        anchorDiffInfo.put("new", newCount);
        anchorDiffInfo.put("delete", deletedCount);
        anchorDiffInfo.put("same", unchangedCount);

        return  anchorDiffInfo;
    }
    /*
<tt>
<i>
<b>
<big>
<small>
<em>
<strong>
<dfn>
<code>
<samp>
<kbd>
<var>
<cite>
<abbr>
<acronym>
<sub>
<sup>
<span>
<bdo>
<address>
<div>
<a>
<object>
<p>
<h1>, <h2>, <h3>, <h4>, <h5>, <h6>
<pre>
<q>
<ins>
<del>
<dt>
<dd>
<li>
<label>
<option>
<textarea>
<fieldset>
<legend>
<button>
<caption>
<td>
<th>
<title>
<script>
<style>
     */



}
