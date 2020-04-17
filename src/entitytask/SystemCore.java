package entitytask;

public class SystemCore {
    private static SystemCore single_instance = null; //Singleton
    private static final int POPULATION_SIZE = 2;
    private static final int LAST_TICK = 5;
    private int actualTick = 0;


    private SystemCore() { }

    /**
     * Starting simulation of evolving population.
     */
    public void run() {
        Populace populace = new Populace();
        populace.addAllEntities(EntityGenerator.generate(populace, POPULATION_SIZE));
        System.out.println("Entry set of entities has been generated...");
        while (LAST_TICK != actualTick) {
            populace.nextTick();
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
