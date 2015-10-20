package toposortparallel;

import java.util.HashMap;
import java.util.concurrent.Callable;

/**
 *
 * @author J. Baum
 */
public class Calculation implements Callable {
    
    private Node root;
    
    private HashMap<Integer,Result> results;
    
    public Calculation(Node root) {
        this.root = root;
        this.results = new HashMap<>();
    }

    @Override
    public Object call() throws Exception {
        processNode(root);

        return results;
        
    }
    
    private void processNode(Node node) {
        System.out.println("Process: " + node.getId());
        int counter = 0;
        for (Node n : node.getNeighbours()) {

            if (n.getValue() <= node.getValue()) {
                n.setValue(node.getValue() + 1);
                results.put(n.getId(), new Result(n, node.getValue() + 1));

                if (counter > 0) {
                    System.out.println("Submit new: " + n.getId());
                    ToposortParallel.cs.submit(new Calculation(n));
                } else {
                    processNode(n);
                }
            }
            System.out.println("nothing to be done at node " + node.getId() + " -> " + n.getId());
            counter++;
        }
    }
    
}
