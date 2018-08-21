/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cowlite.ai.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Wessel
 */
public class Path <T extends State> {
    private double cost;
    private List<T> path;
    private T last;
    private T secondLast;
    
    public Path() {
        cost = 0;
        path = new LinkedList<>();
    }
    
    protected Path(List<T> path, double cost) {
        if(path.contains(null)) {
            throw new IllegalArgumentException("Cannot add null node to a path.");
        }
        this.path = new LinkedList<>(path);
        this.cost = cost;
        this.last = path.get(path.size() - 1);
    }

    public void addNext(T node, double cost) {
        if(node == null) {
            throw new IllegalArgumentException("Cannot add null node to a path.");
        } else if(path.contains(node)) {
            throw new IllegalArgumentException("Cannot add the same node to a path twice.");
        } else if(last != null && !last.getNeighbours().containsKey(node)) {
            throw new IllegalArgumentException("Added node is not a neighbour of last node in path.");
        }
        
        this.cost += cost;
        path.add(node);
        secondLast = last;
        last = node;
    }
    
    public void removeLastNode() {
        path.remove(path.size() - 1);
        last = secondLast;
        secondLast = path.get(path.size() - 2);
    }

    public double getCost() {
        return cost;
    }

    public List<T> getPath() {
        ArrayList<T> returnable = new ArrayList<>();
        returnable.addAll(path);
        return returnable;
    }
    
    public List<T> getReversePath() {
        List<T> returnable = getPath();
        Collections.reverse(returnable);
        return returnable;
    }

    public T getLastNode() {
        return last;
    }
    
    public T getSecondLastNode() {
        return secondLast;
    }
    
    public Path merge(Path<T> p) {
        if(!p.getLastNode().equals(last)) {
            throw new IllegalArgumentException("The paths cannot be merged as the last "
                    + " none of the starting or ending nodes match.");
        }
        
        List<T> path = this.path;
        List<T> otherPath = p.getPath();
        Collections.reverse(otherPath);
        int removed = 0;
        Iterator<T> it = otherPath.iterator();
        while(it.hasNext()){
            if(path.contains(it.next())) {
                it.remove();
                removed++;
            }
        }
        
        for(int i = 0; i < removed - 1; i++) {
            path.remove(path.size() - 1);
        }
        
        Path np = new Path();
        T previous = null;
        
        for(T n : path) {
            if(previous != null) {
                np.addNext(n, previous.getNeighbours().get(n));
            } else {
                np.addNext(n, 0);
            }
            previous = n;
            otherPath.remove(n);
        }
        
        for(T other : otherPath) {
            np.addNext(other, previous.getNeighbours().get(other));

            previous = other;
        }
        
        return np;
    }
    
    public boolean containsMultiple(Path<T> other) {
        int count = 0;
        for(T state : path) {
            if(other.path.contains(state)) {
                count++;
            }
            
            if(count > 1) {
                return true;
            }
        }
        
        return false;
    }
    
    public boolean containsNode(T n) {
        return path.contains(n);
    }
}
