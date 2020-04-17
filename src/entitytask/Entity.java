package entitytask;

import javafx.concurrent.Task;

import java.util.LinkedList;
import java.util.List;
import java.util.SplittableRandom;
import java.util.concurrent.ExecutionException;

public class Entity implements Comparable<Entity> {

    //================================================================================
    // Properties
    //================================================================================
    private Populace populace;
    private Task<Entity> entityTask = null;
    private String name;
    private Double talent;
    private Double sources = 0.0;
    private Entity parentA;
    private Entity parentB;
    private List<Entity> children=new LinkedList<>();

    //================================================================================
    // Constructors
    //================================================================================

    public Entity(Populace mojePopulace) {
        this.populace = mojePopulace;
    }

    /**
     * Copy constructor. This constructor makes hard copy of the object.
     *
     * @param entity object to be copied
     */
    public Entity(Entity entity) {
        // TODO full hard copy
        this.populace = entity.getPopulace();
        this.entityTask = entity.getEntityTask(); //dont need hard copy
        this.name = entity.getName(); //ok
        this.talent = entity.getTalent(); //ok
        this.sources = entity.getSources();//ok
//        this.parentA = entity.getParentA();//nok
        this.parentA = new Entity(entity.getParentA().populace, entity.getParentA().getName(), entity.getParentA().getTalent(), entity.getParentA(), entity.getParentA()); //?? ok ???
        this.parentB = entity.getParentB();//nok
        this.children = entity.getChildren();//nok
    }

    public Entity(Populace populace, String name, Double talent, Entity parentA, Entity parentB) {
        this.populace = populace;
        this.name = name;
        this.talent = talent;
        this.parentA = parentA;
        this.parentB = parentB;
    }

    public Entity(Populace populace, String name, Double talent) {
        this.populace = populace;
        this.name = name;
        this.talent = talent;
        this.children = new LinkedList<>();
    }

    @Override
    public int compareTo(Entity entity) {
        int i = Double.compare(entity.getTalent(), this.talent);
        if (i != 0) {
            return i;
        }
        return Double.compare(entity.getTalent(), this.talent);

    }

    @Override
    public String toString() {
        return "{" + this.name + '}';
    }

    public Task<Entity> createTask() {
        this.entityTask = new Task<>() {
            @Override
            protected Entity call() throws Exception {
                // add salary
                gainSources();
                // reproduce, if enough sources
                reproduce();
                return Entity.this;
            }
        };
        return entityTask;
    }

    private Entity obtainFittest() throws ExecutionException, InterruptedException {
        Entity fittest = this.populace.findFittest();
        if (fittest != this && fittest != null) { //v pripade, ze entita nezazadala sama o sebe
            fittest = fittest.entityTask.get(); //cekame nez dopocita
            return fittest;
        }
        return null;
    }

    //================================================================================
    // Entity activities
    //================================================================================
    // region Activities


    private void gainSources() {
        this.sources += this.talent * 1000;
        System.out.println(this.toString() + " Have " + this.sources+" sources.");
    }

    private void reproduce() throws Exception {
        // Selection of fittest partner for this Entity
        Entity fittest = this.selection();
        System.out.println(this.toString() + " found fittest: " + fittest);
        if (fittest == null) {
            return;
        }
        // Crossover
        Entity child = new Entity(
                this.populace,
                "E" + this.populace.size(),
                this.crossover(fittest),
                this,
                fittest
        );
        //mutation
        this.mutation(child);

        // Tell them they're parents
        this.children.add(child);
        fittest.children.add(child);

        // Population have new member
        this.populace.addEntity(child);
        System.out.println(this.toString() + " are reproducing with" + fittest);
    }

    //Selection
    public Entity selection() throws Exception {
        //Select the most fittest individual
        return this.obtainFittest();
    }


    //Crossover
    public double crossover(Entity fittest) {
        if (fittest == null)
            return 0;

        //Select crossover value
        Double crossOverValueOne = this.getTalent();
        Double crossOverValueTwo = fittest.getTalent();

        //Swap values among parents
        return (crossOverValueOne + crossOverValueTwo) / 2;
    }

    //Mutation
    public void mutation(Entity child) {
        if (child == null)
            return;

        SplittableRandom sr = new SplittableRandom();

        // 0.5% chance to mutate
        if (sr.nextInt(0, 5000) < 5) {
            // anomaly talented Entity
            double crossOverValue = sr.nextDouble(0.0, 11.0);
            child.setTalent(crossOverValue);
        }
    }

    // endregion

    //================================================================================
    // Accessors
    //================================================================================
    // region Accessors

    public Populace getPopulace() {
        return populace;
    }

    public void setPopulace(Populace populace) {
        this.populace = populace;
    }

    public Task<Entity> getEntityTask() {
        return entityTask;
    }

    public void setEntityTask(Task<Entity> entityTask) {
        this.entityTask = entityTask;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getTalent() {
        return talent;
    }

    public void setTalent(Double talent) {
        this.talent = talent;
    }

    public Double getSources() {
        return sources;
    }

    public void setSources(Double sources) {
        this.sources = sources;
    }

    public Entity getParentA() {
        return parentA;
    }

    public void setParentA(Entity parentA) {
        this.parentA = parentA;
    }

    public Entity getParentB() {
        return parentB;
    }

    public void setParentB(Entity parentB) {
        this.parentB = parentB;
    }

    public List<Entity> getChildren() {
        return children;
    }

    public void setChildren(List<Entity> children) {
        this.children = children;
    }
    //endregion

}
