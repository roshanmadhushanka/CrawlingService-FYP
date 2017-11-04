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
        /*
            Construct DOMTreeGenerator using a string
        */
        doc = Jsoup.parse(html);
        Element root = doc.body();
        tree = new TreeNode(idCounter++, root);
        addChildren(tree);
        return tree;
    }

    private TreeNode addChildren(TreeNode parent){
        /*
            Recursively add children to parents and create the DOM Tree
        */
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
        /*
            Print the tree
        */
        return doc.toString();
    }

    public void writeFile(String fileName) throws IOException {
        /*
            Write the html file to a file in HDD
        */
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        writer.write(doc.toString());
        writer.close();
    }

}
