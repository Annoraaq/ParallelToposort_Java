package toposortparallel;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author J. Baum
 */
public class Node {
    
    private int id;
    private int value;
    private Map<Integer, Node> neighbours;
    
    public Node(int id, int value) {
        this.id = id;
        this.value = value;
        neighbours = new HashMap<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public synchronized void setValue(int value) {
        this.value = value;
    }
    
    public void addNeighbour(Node node) {
        neighbours.put(node.getId(), node);
    }
    
    public boolean hasNeighbour(Node node) {
        return neighbours.containsKey(node.getId());
    }
    
    public boolean hasNeighbour(int id) {
        return neighbours.containsKey(id);
    }
    
    public Collection<Node> getNeighbours() {
        return neighbours.values();
    }
    
    public Node getNeighbour(int id) {
        return neighbours.get(id);
    }
    
    
}
