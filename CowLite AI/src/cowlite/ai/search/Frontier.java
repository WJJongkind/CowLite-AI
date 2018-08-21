/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cowlite.ai.search;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 *
 * @author Wessel
 */
public class Frontier <T extends State> {
    private PriorityQueue<Path<T>> orderedFrontier;
    private HashMap<T, Path<T>> visited;
    private boolean flushed;
    
    public Frontier(Comparator<Path<T>> c) {
        this.orderedFrontier = new PriorityQueue(c);
        visited = new HashMap<>();
        flushed = false;
    }
    
    public void placeInFrontier(T newState, Path<T> toState) {
        if(visited.containsKey(newState)) {
            return;
        }
        
        orderedFrontier.add(toState);
    }
    
    public Path<T> mergeShortestPath(Frontier<T> f) {
        Path<T> shortest = null;
        
        for(Map.Entry<T, Path<T>> state : visited.entrySet()) {
            Path<T> match = f.visited.get(state.getKey());
            if(match != null && !match.containsMultiple(state.getValue())) {
                Path<T> newPath = state.getValue().merge(match);
                if(shortest == null || shortest.getCost() > newPath.getCost()) {
                    shortest = newPath;
                }
            }
        }
        
        return shortest;
    }
    
    public Path<T> getPathTo(T state) {
        return visited.get(state);
    }
    
    public T next() {
        Path<T> next = null;
        do {
            next = orderedFrontier.poll();
        } while(visited.containsKey(next.getLastNode()));
        
        if(next == null) {
            return null;
        } else {
            visited.put(next.getLastNode(), next);
            return next.getLastNode();
        }
    }
    
    public Path<T> flush(Frontier<T> otherFrontier) {
        flushed = true;
        Path<T> path = null;
        
        for(Path<T> t : orderedFrontier) {
            Path<T> other = otherFrontier.visited.get(t.getLastNode());
            if(other != null && t.getLastNode().equals(other.getLastNode())) {
               // try {
                    Path<T> np = t.merge(other);
                    if(path == null || np.getCost() < path.getCost()) {
                        path = np;
                    }
               // }catch(Exception e) {

               // }
            }
        }
        
        return path;
    }
    
    public boolean isFlushed() {
        return flushed;
    }
    
    public double getNextCost() {
        Path<T> shortest = orderedFrontier.peek();
        
        if(shortest == null) {
            return Double.MAX_VALUE;
        } else {
            return shortest.getCost();
        }
    }
}
