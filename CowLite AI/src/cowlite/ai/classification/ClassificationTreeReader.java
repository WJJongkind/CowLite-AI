/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cowlite.ai.classification;

import cowlite.io.common.FileDataReader;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class can be used to read trees from files.
 * @author Wessel Jongkind
 */
public class ClassificationTreeReader {
    private static final Pattern REGEX = Pattern.compile("(\\|*)\\[(.*)\\] ([^+-]*) ?([+-])?");
    
    /**
     * Reads a tree from the given file.
     * @param file The file that points to the tree-file.
     * @return The tree.
     * @throws Exception When no trees could be derived.
     */
    public static ClassificationTree readTree(File file) throws Exception {
        FileDataReader reader = new FileDataReader();
        reader.setPath(file.getPath());
        return readTree(reader);
    }
    
    /**
     * Reads a tree from the given FileDataReader
     * @param reader The reader that contains the file-data.
     * @return The tree.
     * @throws Exception When the tree could not be derived.
     */
    public static ClassificationTree readTree(FileDataReader reader) throws Exception {
        HashMap<Integer, ClassificationTreeNode> undevelopedNodes = new HashMap<>();
        
        List<String> lines = reader.getDataStringLines();
        
        for(String s : lines) {
            Matcher m = REGEX.matcher(s);
            
            if(m.find()) {
                addNode(m, undevelopedNodes);
            }
        }
        
        return new ClassificationTree(undevelopedNodes.get(0));
    }
    
    /**
     * Adds a node to the incomplete tree.
     * @param m The matcher containing the data of the new node.
     * @param undevelopedNodes The HashMap containing the incompleted tree branches.
     * @throws Exception When a node couldn't be added.
     */
    private static void addNode(Matcher m, HashMap<Integer, ClassificationTreeNode> undevelopedNodes) throws Exception {                
        ClassificationTreeNode node = new ClassificationTreeNode(m.group(3));
        int order = m.group(1).length();
        
        undevelopedNodes.put(order, node);

        if(order > 0) {
            undevelopedNodes.get(order - 1).addChild(m.group(2), node);
        }
        
        if(m.groupCount() >= 4 && m.group(4) != null && m.group(4).equals("+")) {
            System.out.println("Outcome: true");
            node.setOutcome(true);
        }
        else if(m.groupCount() >= 4 && m.group(4) != null && m.group(4).equals("-")) {
            
            System.out.println("Outcome: false");
            node.setOutcome(false);
        }
    }
}
