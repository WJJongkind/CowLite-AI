/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cowlite.ai.search;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author Wessel
 */
public class Node implements State {
    private HashMap<Node, Double> neighbours;
    private String name;
    
    public Node() {
        neighbours = new HashMap<>();
    }
    
    public Node(String name) {
        neighbours = new HashMap<>();
        this.name = name;
    }
    
    public Node(List<Node> neighbours, boolean ensureBidirectional) {
        if(neighbours != null) {
            if(neighbours.contains(null)) {
                throw new IllegalArgumentException("One or more of the neighbours are null.");
            }
            
            this.neighbours = new HashMap<>();
            
            for(Node n : neighbours) {
                this.neighbours.put(n, 1.0);
            
                if(ensureBidirectional) {
                    if(!n.getNeighbours().containsKey(this)) {
                        n.addNeighbour(this, false);
                    }
                }
            }
        } else {
            this.neighbours = new HashMap<>();
        }
    }
    
    public Node(Map<Node, Double> neighbours, boolean ensureBidirectional) {
        if(neighbours != null) {
            if(neighbours.containsKey(null)) {
                throw new IllegalArgumentException("One or more of the neighbours are null.");
            }
            
            this.neighbours = new HashMap<>();
            this.neighbours.putAll(neighbours);
            
            if(ensureBidirectional) {
                for(Node n : neighbours.keySet()) {
                    if(!n.getNeighbours().containsKey(this)) {
                        n.addNeighbour(this, false, neighbours.get(n));
                    }
                }
            }
        } else {
            this.neighbours = new HashMap<>();
        }
    }
    
    public void addNeighbour(Node node, boolean bidirectional) {
        if(!neighbours.containsKey(node) && node != null) {
            neighbours.put(node, 1.0);
            
            if(bidirectional) {
                node.addNeighbour(this, false);
            }
        } else if(node != null) {
            throw new IllegalArgumentException("The given node is already a neighbour.");
        } else {
            throw new IllegalArgumentException("Cannot add null-values as neighbours.");
        }
    }
    
    public void addNeighbour(Node node, boolean bidirectional, double weight) {
        if(!neighbours.containsKey(node) && node != null) {
            neighbours.put(node, weight);
            
            if(bidirectional && !node.getNeighbours().containsKey(this)) {
                node.addNeighbour(this, false, weight);
            }
        } else if(node != null) {
            throw new IllegalArgumentException("The given node is already a neighbour.");
        } else {
            throw new IllegalArgumentException("Cannot add null-values as neighbours.");
        }
    }
    
    public void addNeighbours(Map<Node, Double> neighbours, boolean bidirectional) {
        for(Node n : neighbours.keySet()) {
            if(n == null) {
                throw new IllegalArgumentException("One or more of the neighbours are null.");
            } else if(!this.neighbours.containsKey(n)) {
                this.neighbours.put(n, neighbours.get(n));
                
                if(bidirectional) {
                    n.addNeighbour(this, false, neighbours.get(n));
                }
            }
        }
    }
    
    public void addNeighbours(List<Node> neighbours, boolean bidirectional) {
        for(Node n : neighbours) {
            if(n == null) {
                throw new IllegalArgumentException("One or more of the neighbours are null.");
            } else if(!this.neighbours.containsKey(n)) {
                this.neighbours.put(n, 1.0);
                
                if(bidirectional) {
                    n.addNeighbour(this, false);
                }
            }
        }
    }
    
    public boolean removeNeighbour(Node node) {
        if(!neighbours.containsKey(node)) {
            return false;
        } else {
            neighbours.remove(node);
            return true;
        }
    }
    
    @Override
    public boolean hasNeighbours() {
        return !neighbours.isEmpty();
    }
    
    @Override
    public boolean isNeighbour(State node) {
        if(node.getClass() != Node.class) {
            return false;
        } else {
            return neighbours.containsKey((Node) node);
        }
    }
    
    @Override
    public Map<Node, Double> getNeighbours() {
        return neighbours;
    }
    
    @Override
    public String toString() {
        if(name != null) {
            return name;
        } else {
            return super.toString();
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.neighbours.values());
        hash = 47 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Node other = (Node) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.neighbours, other.neighbours)) {
            return false;
        }
        return true;
    }
}
