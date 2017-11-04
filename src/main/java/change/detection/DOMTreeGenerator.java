package change.detection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by eranga on 10/24/17.
 */
public class DOMTreeGenerator {
    long idCounter = 0;
    Document doc;
    TreeNode tree;

    public TreeNode createDOMTree(String html){
        doc = Jsoup.parse(html);
        Element root = doc.body();
        tree = new TreeNode(idCounter++, root);
        addChildren(tree);
        return tree;
    }

    // recursively add children to parents and create the DOM Tree
    private TreeNode addChildren(TreeNode parent){
        Element parentElement = parent.getElement();
        if(!parentElement.children().isEmpty()){
            List<Element> childElements = parentElement.children();
            for(Element element: childElements){
                TreeNode child = new TreeNode(idCounter++, element);
                addChildren(child);
                parent.addChild(child);
            }
        }
        return tree;
    }

    public String printTree(){
        return doc.toString();
    }

    public void writeFile(String fileName) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        writer.write(doc.toString());
        writer.close();
    }

}
