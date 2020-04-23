package entitytask;

import java.util.LinkedList;
import java.util.List;

public class SystemCore {
    public static final double CHILD_EXPENSE = 2000.0;
    public static final double SALARY_MULTIPLIER = 100.0;
    public static final int N_THREADS = 2;
    public static final int POPULATION_SIZE = 200;
    public static final int END_TICK = 20;
    public static LinkedList<Entity> INIT_SET;
    private int actualTick = 0;
    private static SystemCore single_instance = null; //Singleton
    private Population population = null;
    private History history = null;

    private SystemCore() {
        population = new Population();
        history = new History(population);
        INIT_SET = EntityGenerator.generate(population, POPULATION_SIZE);
        System.out.println("Entry set of entities has been generated...");
        population.addAllEntities(INIT_SET);
        history.add(INIT_SET);
    }

    /**
     * Starting simulation of evolving population.
     */
    public void run() {
        System.out.println("Starting...");
        while (END_TICK != actualTick) {
            population.nextTick();
            System.out.println("Time tick " + actualTick + " has been done...");
//            System.out.println("---------------------------------------------------------------------------------------");
            actualTick++;
            history.save();
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
        return this.history.getHistory();
    }


}
