/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cowlite.ai.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Wessel
 */
public interface State {
    public boolean hasNeighbours();
    
    public boolean isNeighbour(State node);
    
    public Map<? extends State, Double> getNeighbours();
}
