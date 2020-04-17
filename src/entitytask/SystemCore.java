package entitytask;

public class SystemCore {
    public static final double CHILD_EXPENSE = 2000.0;
    public static final double SALARY_MULTIPLIER = 100.0;
    private static SystemCore single_instance = null; //Singleton
    private static final int POPULATION_SIZE = 2;
    private static final int END_TICK = 10;
    private int actualTick = 0;


    private SystemCore() {
    }

    /**
     * Starting simulation of evolving population.
     */
    public void run() {
        Population population = new Population();
        population.addAllEntities(EntityGenerator.generate(population, POPULATION_SIZE));
        System.out.println("Entry set of entities has been generated...");
        while (END_TICK != actualTick) {
            population.nextTick();
            System.out.println("Time tick " + actualTick + " has been done...");
            System.out.println("---------------------------------------------------------------------------------------");
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
}
