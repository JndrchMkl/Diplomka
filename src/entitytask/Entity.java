package entitytask;

import javafx.concurrent.Task;

import java.util.LinkedList;
import java.util.List;
import java.util.SplittableRandom;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static entitytask.SystemCore.CHILD_EXPENSE;
import static entitytask.SystemCore.SALARY_MULTIPLIER;

public class Entity implements Comparable<Entity>, Callable<Entity> {

    //================================================================================
    // Properties
    //================================================================================
    private Population population;
    private Future<Entity> entityFuture = null;
    private String name;
    private Double talent;
    private Double sources = 0.0;
    private Entity parentA;
    private Entity parentB;
    private List<Entity> children = new LinkedList<>();

    //================================================================================
    // Constructors
    //================================================================================
    //region Constructors

    /**
     * Copy constructor. This constructor makes hard copy of the object.
     *
     * @param entity object to be copied
     */
    public Entity(Entity entity) {
        // TODO full hard copy
        this.population = entity.getPopulation();
        this.entityFuture = entity.getEntityFuture(); //dont need hard copy
        this.name = entity.getName(); //ok
        this.talent = entity.getTalent(); //ok
        this.sources = entity.getSources();//ok
        this.parentA = entity.getParentA();//nok
//        this.parentA = new Entity(entity.getParentA().populace, entity.getParentA().getName(), entity.getParentA().getTalent(), entity.getParentA(), entity.getParentA()); //?? ok ???
        this.parentB = entity.getParentB();//nok
        this.children = entity.getChildren();//nok
    }

    public Entity(Population population, String name, Double talent, Entity parentA, Entity parentB) {
        this.population = population;
        this.name = name;
        this.talent = talent;
        this.parentA = parentA;
        this.parentB = parentB;
    }

    public Entity(Population population, String name, Double talent) {
        this.population = population;
        this.name = name;
        this.talent = talent;
        this.children = new LinkedList<>();
    }

    //endregion


    //================================================================================
    // General operation
    //================================================================================
    @Override
    public Entity call() throws Exception {
        // add salary
        gainSources();
        // reproduce, if enough sources
        reproduce();
        return Entity.this;
    }

    private Entity obtainFittest() throws ExecutionException, InterruptedException, BrokenBarrierException {
        System.out.println("Blocking " + this);
        Entity fittest = this.population.findFittest(Entity.this);
        System.out.println("Unblocking after find " + this);
        if (fittest != null) { //v pripade, ze entita nezazadala sama o sebe
            System.out.println("Blocking before call " + this);
            fittest = fittest.entityFuture.get(); //cekame nez dopocita
            System.out.println("Unblocking after call " + this);
            return fittest;
        }
        return null;
    }

    @Override
    public String toString() {
        return "{" + this.name + '}';
    }

    @Override
    public int compareTo(Entity entity) {
        int i = Double.compare(entity.getTalent(), this.talent);
        if (i != 0) {
            return i;
        }
        return Double.compare(entity.getTalent(), this.talent);

    }

    //================================================================================
    // Entity operation
    //================================================================================
    // region Operation


    private void gainSources() {
        this.sources = sources + (this.talent * SALARY_MULTIPLIER);
        System.out.println(this.toString() + " Have " + this.sources + " sources.");
    }

    private void reproduce() throws Exception {
        // Selection of fittest partner for this Entity
        Entity fittest = this.selection();
        System.out.println(this.toString() + " found fittest: " + fittest);
        if (fittest == null || this.getSources() < CHILD_EXPENSE) {
            return;
        }
        // Crossover
        Entity child = crossover(fittest);

        //mutation
        this.mutation(child);

        // Tell them they're parents
        this.children.add(child);
        fittest.children.add(child);

        // and have expenses...
        this.setSources(sources - CHILD_EXPENSE);
        fittest.setSources(fittest.getSources() - CHILD_EXPENSE);
        // Population have new member
        this.population.addEntity(child);
        System.out.println(this.toString() + " are reproducing with " + fittest + " and they have " + child);
    }

    //Selection
    public Entity selection() throws Exception {
        //Select the most fittest individual
        return this.obtainFittest();
    }

    //Crossover
    public synchronized Entity crossover(Entity fittest) {
        if (fittest == null)
            return null;

        //Select crossover value
        Double crossOverValueOne = this.getTalent();
        Double crossOverValueTwo = fittest.getTalent();
        Entity child = new Entity(
                this.population,
                "E" + this.population.size(),
                (crossOverValueOne + crossOverValueTwo) / 2,
                this,
                fittest
        );
        return child;
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

    public Population getPopulation() {
        return population;
    }

    public void setPopulation(Population population) {
        this.population = population;
    }

    public Future<Entity> getEntityFuture() {
        return entityFuture;
    }

    public void setEntityFuture(Future<Entity> entityFuture) {
        this.entityFuture = entityFuture;
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
