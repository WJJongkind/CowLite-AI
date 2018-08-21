/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cowlite.ai.classification;

import java.util.Map;

/**
 * Objects of this class represent trees that can be traversed.
 * @author Wessel Jongkind
 */
public class ClassificationTree 
{
    private final ClassificationTreeNode root;
    
    /**
     * Instantiates a new tree.
     * @param root The node that is the root of the tree.
     */
    public ClassificationTree(ClassificationTreeNode root)
    {
        this.root = root;
    }

    /**
     * Returns the root of the tree.
     * @return The root of the tree.
     */
    public ClassificationTreeNode getRoot() 
    {
        return root;
    }
    
    /**
     * Checks if the tree contains the given node.
     * @param node The node to be checked.
     * @return True if the given node is in the tree.
     */
    public boolean containsNode(ClassificationTreeNode node)
    {
        return root == node || root.isAncestorOf(node);
    }
    
    @Override
    public String toString()
    {
        return buildChildString(root, "","");
    }
    
    private String buildChildString(ClassificationTreeNode node, String result, String spacer)
    {
        result += spacer + "[" + node.getValue() + "]" + " " + node.getName() + "\n";
        Map<String, ClassificationTreeNode> children = node.getChildren();
        for(Map.Entry<String, ClassificationTreeNode> child : children.entrySet())
            result += buildChildString(child.getValue(), "", spacer + "   |   ");
        return result;
    }
}
