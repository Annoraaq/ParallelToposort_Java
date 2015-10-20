package toposortparallel;

import java.util.HashMap;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author J. Baum
 */
public class ToposortParallel {
    
    public static ExecutorService es = Executors.newFixedThreadPool(10);
    public static CompletionService<HashMap<Integer,Result>> cs;
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        cs = new ExecutorCompletionService<>(es);
        Algorithm alg = new Algorithm();
        alg.init();
        alg.run();
        
    }
    
}
