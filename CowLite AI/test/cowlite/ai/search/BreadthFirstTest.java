/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cowlite.ai.search;

import cowlite.ai.search.BreadthFirst;
import cowlite.ai.search.Node;
import cowlite.ai.search.Path;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Wessel
 */
public class BreadthFirstTest {
    //TODO to be implemented. Code below contains two decent graphs for extensive testing.
    @Test
    public void testMonodirectionalSearch() {
        Node a = new Node("a");
        Node b = new Node("b");
        Node c = new Node("c");
        Node d = new Node("d");
        Node e = new Node("e");
        Node f = new Node("f");
        Node g = new Node("g");
        Node h = new Node("h");
        Node i = new Node("i");
        Node j = new Node("j");
        Node k = new Node("k");
        Node l = new Node("l");
        Node m = new Node("m");
        Node n = new Node("n");
        
        a.addNeighbour(c, true, 13);
        a.addNeighbour(h, true, 2);
        
        b.addNeighbour(c, true, 1);
        b.addNeighbour(d, true, 14);
        
        c.addNeighbour(d, true, 11);
        
        d.addNeighbour(e, true, 9);
        d.addNeighbour(g, true, 6);
        
        e.addNeighbour(h, true, 5);
        
        f.addNeighbour(g, true, 1);
        
        g.addNeighbour(h, true, 9);
        
        h.addNeighbour(i, true, 5);
        
        i.addNeighbour(j, true, 10);
        
        j.addNeighbour(l, true, 7);
        j.addNeighbour(k, true, 8);
        
        k.addNeighbour(m, true, 3);
        
        l.addNeighbour(m, true, 5);
        
        m.addNeighbour(n, true, 2);
        
        Path path = BreadthFirst.searchPath(d, k);
        assertEquals(37.0, path.getCost(), 0.1);
    }
        /*
        Path path = bidirectionalSearchPath(d, k);
        System.out.println("=-=-=-=-=-=-");
        System.out.println(path.getCost());
        for(Object no : path.getPath()) {
            System.out.println(no);
        }
        //path = searchPath(d, k);
        System.out.println("=-=-=-=-=-=-");
        System.out.println(path.getCost());
        for(Object no : path.getPath()) {
            System.out.println(no);
        }
        
        
        
        
         a = new Node("a");
         b = new Node("b");
         c = new Node("c");
         d = new Node("d");
         e = new Node("e");
         f = new Node("f");
         g = new Node("g");
        
        b.addNeighbour(a, true, 83);
        c.addNeighbour(b, true, 71);
        c.addNeighbour(a, true, 38);
        d.addNeighbour(b, true, 81);
        e.addNeighbour(b, true, 32);
        e.addNeighbour(a, true, 66);
        f.addNeighbour(e, true, 74);
        f.addNeighbour(c, true, 36);
        g.addNeighbour(f, true, 45);
        g.addNeighbour(c, true, 5);

        Long st = System.currentTimeMillis();
        System.out.println("Starting...");
        Path path1 = bidirectionalSearchPath(a, b);
        
        for(Object t : path1.getPath()) {
            System.out.println(t);
        }
        st = System.currentTimeMillis();
        System.out.println(".");
        Path path2 = searchPath(a, b);
        for(Object t : path2.getPath()) {
            System.out.println(t);
        }
        System.out.println("=-=-=-=-=-=-");

        System.out.println(path2.getCost() + "  "  + path1.getCost());*/
}
