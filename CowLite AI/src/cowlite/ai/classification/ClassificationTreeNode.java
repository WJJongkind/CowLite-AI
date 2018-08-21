/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cowlite.ai.classification;

import java.util.HashMap;
import java.util.Map;

/**
 * Objects of this class represent nodes in trees.
 * @author Wessel Jongkind
 * @param <T> The datatype of the node.
 */
public class ClassificationTreeNode <T>
{
    private ClassificationTreeNode parent;
    private final String name;
    private T value;
    private boolean outcome;
    private final HashMap<T, ClassificationTreeNode> children = new HashMap<>();
    
    /**
     * Instantiate a new node with a given name
     * @param name The name of the node
     */
    public ClassificationTreeNode(String name)
    {
        this.name = name;
    }
    
    /**
     * Adds a child to the node.
     * @param pathValue The value of the path that leads to the child.
     * @param child The child node.
     * @throws Exception When the child can not be added.
     */
    public void addChild(T pathValue, ClassificationTreeNode child) throws Exception
    {
        if(child == this)
            throw new Exception("You can't add a node to itself as a child.");
        
        if(parent == null || !hasAncestor(child))
        {
            if(!children.containsKey(pathValue))
            {
                children.put(pathValue, child);
                child.setParent(this, pathValue);
            }
            else
                throw new Exception("This path already has a child.");
        }
        else
            throw new Exception("The node is an ancestor of the current node.");
    }
    
    /**
     * Sets the parent of the node.
     * @param parent The parent of the node.
     * @param value The value of the path that leads to the parent.
     * @throws Exception When the parent cant be set
     */
    protected void setParent(ClassificationTreeNode parent, T value) throws Exception
    {
        if(parent == this)
            throw new Exception("Can't make a node parent of itself.");
        
        this.parent = parent;
        this.value = value;
    }
    
    /**
     * If the node is a leaf, the outcome can be either true or positive for the
     * case that the tree is deciding for.
     * @param outcome 
     */
    public void setOutcome(boolean outcome)
    {
        this.outcome = outcome;
    }
    
    /**
     * Checks whether or not the node has the given node as an ancestor.
     * @param node The node which should be tested as ancestor.
     * @return True if the given node is an ancestor.
     */
    public boolean hasAncestor(ClassificationTreeNode node)
    {
        return parent != null && (parent.getName().equals(node.getName()) || parent.hasAncestor(node));
    }
    
    /**
     * Checks if the node is an ancestor of the given node.
     * @param node The child-node
     * @return True of the node is an ancestor of the given node.
     */
    public boolean isAncestorOf(ClassificationTreeNode node)
    {
        return children.containsValue(node) || checkChildrenAncestorOf(node);
    }
    
    /**
     * Checks whether or not one or more of the children are an ancestor of a given
     * node.
     * @param node The node which should be checked.
     * @return True if one or more of the children are an ancestor of the given node.
     */
    private boolean checkChildrenAncestorOf(ClassificationTreeNode node)
    {
        for(Map.Entry<T, ClassificationTreeNode> child : children.entrySet())
            if(child.getValue().isAncestorOf(node))
                return true;
        return false;
    }
    
    /**
     * Checks if the given node is a child of the node.
     * @param node The node which should be checked.
     * @return True if the given node is a child.
     */
    public boolean hasChild(ClassificationTreeNode node)
    {
        return children.containsValue(node);
    }

    /**
     * Returns the parent of the node.
     * @return The parent of the node.
     */
    public ClassificationTreeNode getParent() 
    {
        return parent;
    }

    /**
     * Returns the name of the node.
     * @return The name of the node.
     */
    public String getName() 
    {
        return name;
    }

    /**
     * True if the node is a leaf.
     * @return True if the node is a leaf.
     */
    public boolean isLeaf() 
    {
        return children.isEmpty();
    }
    
    /**
     * Returns the final outcome of the node if the node is a leaf.
     * @return The outcome of the tree.
     */
    public boolean getLeafOutcome()
    {
        return outcome;
    }

    /**
     * Returns the children of the node.
     * @return The children.
     */
    public HashMap<T, ClassificationTreeNode> getChildren() 
    {
        return children;
    }
    
    /**
     * Returns the child of this node that is linked to the given path value.
     * @param value The value of the path that leads to the child.
     * @return The child.
     */
    public ClassificationTreeNode getChild(String value)
    {
        return children.get(value);
    }
    
    /**
     * Returns the value of the node.
     * @return The value of the node.
     */
    public T getValue()
    {
        return value;
    }
}
