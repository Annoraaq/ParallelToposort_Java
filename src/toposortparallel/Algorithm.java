package toposortparallel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author J. Baum
 */
public class Algorithm {
    
    private Node root;
    private Node n9;

    
    
    public void init() {
        root = new Node(1,1);
        Node n2 = new Node(2,0);
        Node n3 = new Node(3,0);
        Node n4 = new Node(4,0);
        Node n5 = new Node(5,0);
        Node n6 = new Node(6,0);
        Node n7 = new Node(7,0);
        Node n8 = new Node(8,0);
        n9 = new Node(9,1);
        
        root.addNeighbour(n3);
        n2.addNeighbour(n8);
        n3.addNeighbour(n7);
        n4.addNeighbour(n6);
        n5.addNeighbour(n8);
        n7.addNeighbour(n4);
        n7.addNeighbour(n5);
        n8.addNeighbour(n6);
        n9.addNeighbour(n2);
        n9.addNeighbour(n5);
    }
    
    public void run() {
        
        Map<Integer,Node> toRepeat = new HashMap<>();
        Map<Integer,List<Result>> results = new HashMap<>();
        
        ToposortParallel.cs.submit(new Calculation(root));
        ToposortParallel.cs.submit(new Calculation(n9));

        try {
            Future<HashMap<Integer,Result>> abc;
            HashMap<Integer,Result> res;
            abc = ToposortParallel.cs.take();
            boolean finished = false;
            
            while (!finished) {
                
                for (int key : toRepeat.keySet()) {
                    
                    int max = 0;
                    for (Result re : results.get(key)) {
                        if (re.value > max) {
                            max = re.value;
                        }
                    }
                    toRepeat.get(key).setValue(max);
                    
                    ToposortParallel.cs.submit(new Calculation(toRepeat.get(key)));
                }
                toRepeat = new HashMap<>();
                results = new HashMap<>();
                
            while (abc != null) {
                res = abc.get();
                
                System.out.println("Results:");
                for (int key : res.keySet()) {
                    System.out.println(key + "=" + res.get(key).value);
                    if (results.containsKey(key)) {
                        results.get(key).add(new Result(res.get(key).node, res.get(key).value));
                        toRepeat.put(key, res.get(key).node);
                    } else {
                        ArrayList<Result> tempList = new ArrayList<>();
                        tempList.add(new Result(res.get(key).node, res.get(key).value));
                        results.put(key, tempList);
                    }
                }

                abc = ToposortParallel.cs.poll(1, TimeUnit.SECONDS);
            }
            
            System.out.println("left while loop");
            
            if (toRepeat.keySet().isEmpty()) {
                finished = true;
            }
            
            }
            
            
            ToposortParallel.es.shutdown();
            
            int max = 0;
            for (int key : results.keySet()) {
                for (Result re : results.get(key)) {
                    if (re.value > max) {
                        max = re.value;
                    }
                }

                results.get(key).get(0).node.setValue(max);
            }
            
            System.out.println();
            printNodeRec(root);
            System.out.println();
            printNodeRec(n9);
        
 
        } catch (InterruptedException ex) {
            System.err.println(ex.getMessage());
        } catch (ExecutionException ex) {
            System.err.println(ex.getMessage());
        }
   
    }
    
    private void printNodeRec(Node node) {
        System.out.println("id: " + node.getId() + ", value: " + node.getValue());
        for (Node neigh : node.getNeighbours()) {
            printNodeRec(neigh);
        }
    }
    
}
