package cowlite.ai.search;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.Random;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



/**
 *
 * @author Wessel
 */
public class BreadthFirst {
    public static Path searchPath(State start, State target) {
        if(start == null || target == null) {
            throw new IllegalArgumentException("Start or target node is null.");
        }
        
        BreadthFirstSearcher searcher = new BreadthFirstSearcher(start);
        Frontier targetFrontier = new Frontier(new PathComparator());
        Path tgt = new Path();
        tgt.addNext(target, 0);
        targetFrontier.placeInFrontier(target, tgt);
        targetFrontier.next();
        
        Path path = null;
        do {
            path = searcher.findPath(targetFrontier, true, path != null);
            //System.out.println(path);
        } while(path == null && !searcher.isFinished());
        
        return path;
    }
    
    public static Path bidirectionalSearchPath(State start, State target) {
        if(start == null || target == null) {
            throw new IllegalArgumentException("Start or target node is null.");
        }
        
        BreadthFirstSearcher targetSearcher = new BreadthFirstSearcher(start);
        Frontier targetFrontier = new Frontier(new PathComparator());
        Path tgt = new Path();
        targetFrontier.placeInFrontier(target, tgt);
        
        BreadthFirstSearcher startSearcher = new BreadthFirstSearcher(target);
        
        Path shortest = null;
        BreadthFirstSearcher searcher = targetSearcher;
        Frontier f = targetFrontier;
        do {
            Path path = searcher.findPath(f, searcher == targetSearcher, shortest != null);
            if(shortest == null || (path != null && path.getCost() < shortest.getCost())) {
                shortest = path;
            }
            
            f = searcher.frontier;
            if(searcher == targetSearcher) {
                searcher = startSearcher;
            } else {
                searcher = targetSearcher;
            }
        } while(shortest == null ||
               ((targetSearcher.getLowestCost() + startSearcher.getLowestCost() < shortest.getCost()) || (!startSearcher.isFlushed() && !targetSearcher.isFlushed()) &&
               !targetSearcher.isFinished() && !startSearcher.isFinished()));
        
        return shortest;
    }
    
    private static class PathComparator implements Comparator<Path> {
        @Override
        public int compare(Path o1, Path o2) {
            double val = o1.getCost() - o2.getCost();
            if(val < 0) {
                return (int) Math.floor(val);
            } else if(val > 0) {
                return (int) Math.ceil(val);
            } else {
                return 0;
            }
        }
    }
    
    private static class BreadthFirstSearcher {
        private Frontier frontier;
        private boolean finished;
        
        public BreadthFirstSearcher(State start) {
            frontier = new Frontier(new PathComparator());
            Path path = new Path();
            path.addNext(start, 0);
            frontier.placeInFrontier(start, path);
            this.finished = false;
        }
        
        public Path findPath(Frontier otherFrontier, boolean fromStart, boolean flush) {
            if(flush) {
                System.out.println("Flushing...");
                if(fromStart) {
                    return frontier.flush(otherFrontier);
                } else {
                    return otherFrontier.flush(frontier);
                }
            }
            
            State next = frontier.next();
            Path path = frontier.getPathTo(next);
            
            if(next == null) {
                finished = true;
                return null;
            }
            //System.out.println("visiting: " + next + " " + path.getCost());
            
            for(Map.Entry<? extends State, Double> entry : next.getNeighbours().entrySet()) {
                //System.out.print("Child: " + entry.getKey());
                if(!path.containsNode(entry.getKey())) {
                    //System.out.print(" has been added");
                    Path toState = new Path(path.getPath(), path.getCost());
                    toState.addNext(entry.getKey(), entry.getValue());
             //       System.out.println("Adding: " + entry.getKey() + "   " + toState.getCost());

                    frontier.placeInFrontier(entry.getKey(), toState);
                }
                //System.out.println();
            }
            
            if(otherFrontier.getPathTo(next) != null) {
                if(fromStart) {
                    return frontier.mergeShortestPath(otherFrontier);
                } else {
                    return otherFrontier.mergeShortestPath(frontier);
                }
            } else {
                return null;
            }
        }

        public boolean isFinished() {
            return finished;
        }
        
        public boolean isFlushed() {
            return frontier.isFlushed();
        }
        
        public double getLowestCost() {
            return frontier.getNextCost();
        }
    }
}
