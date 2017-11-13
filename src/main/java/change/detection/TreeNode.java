package change.detection;

import org.apache.commons.text.similarity.CosineDistance;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eranga on 10/24/17.
 */
public class TreeNode{
    private Element element = null;
    private long id;
    private List<TreeNode> children = new ArrayList();
    private TreeNode parent = null;

    public TreeNode(long id, Element element) {
        this.element = element;
        this.id = id;
    }

    public void addChild(TreeNode child) {
        child.setParent(this);
        this.children.add(child);
    }

    public void addChild(long id, Element element) {
        TreeNode newChild = new TreeNode(id, element);
        newChild.setParent(this);
        children.add(newChild);
    }

    public void addChildren(List<TreeNode> children) {
        for(TreeNode node: children) {
            node.setParent(this);
        }
        this.children.addAll(children);
    }

    public double cosineDistance(TreeNode node){
        /*
            Compute Cosine Distance from node to this
        */
        // checking whether the text inside the element is empty
        if(element.text().equals("") || node.getElement().text().equals(""))
            return -1;
        // end checking
        if(element.text().equals(node.getElement().text()))
            return 0.0;
        return new CosineDistance().apply(node.getElement().text(), element.text());
    }

    public List<TreeNode> getChildrenByTag(Tag tag){
        /*
            Return the list of children by the specific tag
        */
        List<TreeNode> list = new ArrayList();
        for(TreeNode node: children){
            if(node.getElement().tag().equals(tag))
                list.add(node);
        }
        return list;
    }

    public List<Tag> getChildTags(){
        /*
            Return the list of tags of children
        */
        List<Tag> tags = new ArrayList();
        for(TreeNode node: children){
            if(!tags.contains(node.getElement().tag()))
                tags.add(node.getElement().tag());
        }
        return tags;
    }

    public TreeNode getChildById(long id){
        /*
            Recursively search and return the child with the given id
        */
        if(this.id == id)
            return this;
        else{
            long[] ids = getChildIds();
            for(int i=ids.length-1; i>=0; i--){
                if(ids[i] < id)
                    return children.get(i).getChildById(id);
                if(ids[i] == id)
                    return children.get(i);
            }
        }
        return null;
    }

    public long[] getChildIds(){
       /*
            Return the list of ids of children
       */
       long[] ids = new long[children.size()];
       int count = 0;
       for(TreeNode node: children){
           ids[count++] = node.getId();
       }
       return ids;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    private void setParent(TreeNode parent) {
        this.parent = parent;
    }

    public TreeNode getParent() {
        return parent;
    }

    public long getId() {
        return id;
    }
}
