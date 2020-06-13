package entitytask;

import java.util.LinkedList;
import java.util.List;

public class SystemCore {
    private static SystemCore single_instance = null; //Singleton
    public static final double CHILD_EXPENSE = 2000.0;
    public static final double SALARY_MULTIPLIER = 100.0;
    public static final int N_THREADS = 2;
    public static final int POPULATION_SIZE = 2;
    public static final int END_TICK = 10;
    public static LinkedList<Entity> INIT_SET;
    private Population population = null;
    private History history = null;
    private TimeLine timeLine = null;

    private SystemCore() {
        population = new Population();
        history = new History(population);
        timeLine = new TimeLine();
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
        while (END_TICK != timeLine.getActualTick()) {
            population.nextTick();
            System.out.println("Time tick " + timeLine.getActualTick() + " has been done...");
            timeLine.timeIncrement();
            history.save();
        }
        System.out.println("Ending...");
    }

    public List<List<Entity>> history() {
        return this.history.getHistory();
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


}
