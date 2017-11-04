package change.detection;

import javafx.util.Pair;
import org.jsoup.parser.Tag;

import java.io.*;
import java.util.*;

/**
 * Created by eranga on 10/22/17.
 */
public class ChangeDetector {

    static List<Long> deleted = new ArrayList();
    static List<Long> newlyAdded = new ArrayList();
    static List<Long> updated1 = new ArrayList();
    static List<Long> updated2 = new ArrayList();
    static double maxCosineDistance = 0.8;

    public enum Color{
        RED ("#FF0000"), YELLOW ("#FFFF00"), GREEN ("#32CD32");
        public String colorCode;
        Color(String colorCode) {
            this.colorCode = colorCode;
        }
    }

    public String[] processChanges(String html1, String html2){
        /*
            Returns the input html strings with patches
        */
        DOMTreeGenerator domTreeGenerator1 = new DOMTreeGenerator();
        TreeNode tree1 = domTreeGenerator1.createDOMTree(html1);
        DOMTreeGenerator domTreeGenerator2 = new DOMTreeGenerator();
        TreeNode tree2 = domTreeGenerator2.createDOMTree(html2);

        detectChanges(tree1, tree2);
        processNodes(tree1, tree2);
        return new String[]{domTreeGenerator1.printTree(), domTreeGenerator2.printTree()};
    }

    private void detectChanges(TreeNode root1, TreeNode root2){
        /*
            Detect changes of the DOM structures
        */
        // If roots are equal do nothing
        if(root1.cosineDistance(root2) == 0.0)
            return;
        else if(!root1.getChildren().isEmpty() && !root2.getChildren().isEmpty()){
            Set<Tag> childTags = new HashSet(root1.getChildTags());
            childTags.addAll(root2.getChildTags());
            // Loop for all different type of tags in both the roots
            for(Tag tag: childTags) {
                List<TreeNode> children1 = root1.getChildrenByTag(tag);
                List<TreeNode> children2 = root2.getChildrenByTag(tag);
                // check if root1 has children with the particular tag type
                if(children1.isEmpty()){
                    children2.forEach(element -> newlyAdded.add(element.getId()));
                    continue;
                // check if root2 has children with the particular tag type
                } else if(children2.isEmpty()){
                    children1.forEach(element -> deleted.add(element.getId()));
                    continue;
                } else {
                    int n = children1.size();
                    int m = children2.size();
                    double[][] distanceMatrix = new double[n][m];
                    TreeMap<Double, ArrayList<Pair<Integer,Integer>>> distanceToIndexMap = new TreeMap();
                    for(int i=0; i<n ; i++){
                        for(int j=0; j<m; j++){
                            distanceMatrix[i][j] = children1.get(i).cosineDistance(children2.get((j)));
                            ArrayList<Pair<Integer,Integer>> mapping = distanceToIndexMap.get(distanceMatrix[i][j]);
                            if(mapping == null)
                                mapping = new ArrayList();
                            mapping.add(new Pair(i, j));
                            distanceToIndexMap.put(distanceMatrix[i][j], mapping);
                        }
                    }
                    Set<Integer> visited1 = new HashSet<>();
                    Set<Integer> visited2 = new HashSet<>();
                    while(distanceToIndexMap.size() > 0){
                        double curKey = distanceToIndexMap.firstKey();
                        ArrayList<Pair<Integer,Integer>> equalElements = distanceToIndexMap.remove(curKey);
                        if(curKey == 0.0){
                            for(Pair<Integer,Integer> p: equalElements){
                                visited1.add(p.getKey());
                                visited2.add(p.getValue());
                            }
                        }else if(curKey > maxCosineDistance){
                            int key, value;
                            for(Pair<Integer,Integer> p: equalElements){
                                key = p.getKey();
                                value = p.getValue();
                                if(visited1.contains(key) || visited2.contains(value)){
                                    continue;
                                }else{
                                    visited1.add(p.getKey());
                                    visited2.add(p.getValue());
                                    deleted.add(children1.get(key).getId());
                                    newlyAdded.add(children2.get(value).getId());
                                }
                            }
                        }else if(curKey > 0){
                            int key, value;
                            for(Pair<Integer,Integer> p: equalElements){
                                key = p.getKey();
                                value = p.getValue();
                                if(visited1.contains(key) || visited2.contains(value)){
                                    continue;
                                }else{
                                    visited1.add(key);
                                    visited2.add(value);
                                    TreeNode newRoot1 = children1.get(key);
                                    TreeNode newRoot2 = children2.get(value);
                                    detectChanges(newRoot1, newRoot2);
                                }
                            }
                        }
                    }
                    // filter the child elements which are unique to root1
                    for(int i=0; i<children1.size(); i++){
                        if(!visited1.contains(i))
                            deleted.add(children1.get(i).getId());
                    }
                    // filter the child elements which are unique to root2
                    for(int i=0; i<children2.size(); i++){
                        if(!visited2.contains(i))
                            newlyAdded.add(children2.get(i).getId());
                    }
                }
            }
        }else if(root1.getChildren().isEmpty() && root2.getChildren().isEmpty()){
            updated1.add(root1.getId());
            updated2.add(root2.getId());
        }
    }

    private void processNodes(TreeNode root1, TreeNode root2){
        /*
            Highlight texts of deleted, newly added and updated nodes
        */
        for(int i=0; i<deleted.size(); i++){
            TreeNode node = root1.getChildById(deleted.get(i));
            highlight(node, Color.RED);
        }
        for(int i=0; i<newlyAdded.size(); i++){
            TreeNode node = root2.getChildById(newlyAdded.get(i));
            highlight(node, Color.GREEN);
        }
        for(int i=0; i<updated1.size(); i++){
            TreeNode node1 = root1.getChildById(updated1.get(i));
            TreeNode node2 = root2.getChildById(updated2.get(i));
            String[] patched = new Diff().diff(node1.getElement().text(), node2.getElement().text());
            node1.getElement().html(patched[0]);
            node2.getElement().html(patched[1]);
        }

    }

    private void highlight(TreeNode node, Color color){
        /*
            Highlight a whole node
        */
        if(!node.getChildren().isEmpty()){
            List<TreeNode> children = node.getChildren();
            children.forEach(child -> highlight(child, color));
        }else{
            node.getElement().html("<mark style=\"background-color:" + color.colorCode + ";\"> "
                    + node.getElement().text() + " </mark>");
        }
    }

    private String readFile(String fileName) throws IOException {
        /*
            Read a file from the HDD and return as a string
        */
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            return sb.toString();
        } finally {
            br.close();
        }
    }
}

