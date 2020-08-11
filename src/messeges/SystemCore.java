package messeges;



import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

public class SystemCore {
    private static SystemCore single_instance = null; //Singleton
    public static final double CHILD_EXPENSE = 2000.0;
    public static final double SALARY_MULTIPLIER = 100.0;
    public static final int N_THREADS = 2;
    public static final int POPULATION_SIZE = 2;
    public static final int END_TICK = 50;
    public static LinkedList<Entity> INIT_SET;
    private Population population = null;
    private History  history = null;

    private SystemCore() {
        population = new Population();
        history = new History(population);
        INIT_SET = EntityGenerator.generate(population, POPULATION_SIZE);
        System.out.println("Entry set of entities has been generated...");
        population.addAllEntities(INIT_SET);

    }


    /**
     * Starting simulation of evolving population.
     */
    public void run() throws InterruptedException {
        System.out.println("Starting...");
        List<Entity> entries = population.state();
        int i =0;


        ExecutorService executor = Executors.newFixedThreadPool(2);
        CompletionService<Entity> compService = new ExecutorCompletionService<>(executor);
        for (Entity e : entries) {
            compService.submit((Callable<Entity>) e);
            compService.take();
            System.out.println(++i);
        }
        System.out.println("Ending...");
    }


    /**
     * Creates instance of SystemCore class
     *
     * @return instance with actual state
     */
    public static SystemCore getInstance() {
        if (single_instance == null)
            single_instance = new SystemCore();

        return single_instance;
    }


    public List<List<Entity>> history() {
        return history.getHistory();
    }
}
