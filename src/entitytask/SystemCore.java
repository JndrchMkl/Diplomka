package entitytask;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class SystemCore {
    public static final double CHILD_EXPENSE = 2000.0;
    public static final double SALARY_MULTIPLIER = 100.0;
    public static final int N_THREADS = 2;
    private static final int POPULATION_SIZE = 200;
    public static final int END_TICK = 20;
    private int actualTick = 0;
    private static SystemCore single_instance = null; //Singleton
    private Population population = null;
    private final List<List<Entity>> history = Collections.synchronizedList(new LinkedList<>());

    private SystemCore() {
        population = new Population();
        LinkedList<Entity> intitSet = EntityGenerator.generate(population, POPULATION_SIZE);
        population.addAllEntities(intitSet);
        history.add(intitSet);
    }

    /**
     * Starting simulation of evolving population.
     */
    public void run() {
        System.out.println("Entry set of entities has been generated...");
        while (END_TICK != actualTick) {
           population.nextTick();
            System.out.println("Time tick " + actualTick + " has been done...");
//            System.out.println("---------------------------------------------------------------------------------------");
            actualTick++;
        }
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

    public List<List<Entity>> history(){
        return this.history;
    }
}
