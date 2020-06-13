package messeges;

import entitytask.SystemCore;
import javafx.concurrent.Task;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

import static entitytask.SystemCore.CHILD_EXPENSE;
import static entitytask.SystemCore.SALARY_MULTIPLIER;
import static java.util.stream.Collectors.toList;

public class Entity extends Task<Entity> implements Comparable<Entity> {

    //================================================================================
    // Properties
    //================================================================================
    private Population population;
    private String name;
    private Double talent;
    private Double sources = 0.0;
    private Entity parentA;
    private Entity parentB;
    private List<Entity> children = new LinkedList<>();
    boolean alive = true;
    private List<Message> messages = new LinkedList<>();


    private Entity bestPartner;

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
        this.population = entity.getPopulation(); //dont need hard copy
        this.name = entity.getName(); //ok
        this.talent = entity.getTalent(); //ok
        this.sources = entity.getSources();//ok
        this.parentA = entity.getParentA();
        this.parentB = entity.getParentB();
//        Entity parentA = entity.getParentA();
//        Entity parentB = entity.getParentB();
//        this.parentA = parentA == null ? null : new Entity(parentA.getPopulation(), parentA.getName(), parentA.getTalent(), parentA, parentB);
//        this.parentB = parentB == null ? null : new Entity(parentB.getPopulation(), parentB.getName(), parentB.getTalent(), parentA, parentB);
        this.children = entity.getChildren().stream().map(Entity::new).collect(toList());

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
        while (alive) {
            if (messages.size() > 0) {
                Message m = messages.remove(0);
                m.process();
            } else {
                gainSources();
                //LIVE minus sources

                // do babies
                if (this.hasEnoughtSources()) {
                    //find best parner for you
                    System.out.println(this + " searching");
                    Entity partner = findPartner();
                    if (partner != null) {
                        // send request
                        Message request = new RequestBaby(this, partner);
                        //block sources this and partner
                        partner.setSources(partner.getSources() - CHILD_EXPENSE);
                        this.setSources(this.getSources() - CHILD_EXPENSE);

                        partner.addMessage(request);
                    }
                }
            }
        }
        return Entity.this;
    }

    private boolean hasEnoughtSources() {
        return this.getSources() > SystemCore.CHILD_EXPENSE;
    }

    private void gainSources() {
        this.sources = sources + (this.talent * SALARY_MULTIPLIER);
    }

    private Entity findPartner() throws ExecutionException, InterruptedException, BrokenBarrierException, TimeoutException {
        Entity fittest = population.findFittest(Entity.this);
        if (fittest != null) { //v pripade, ze entita nezazadala sama o sebe
            this.bestPartner = fittest;
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
        return Double.compare(entity.getSources(), this.sources);

    }

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


    public Entity getBestPartner() {
        return bestPartner;
    }

    public void setBestPartner(Entity bestPartner) {
        this.bestPartner = bestPartner;
    }


    //endregion

    public void addMessage(Message m) {
        if (alive) {
            messages.add(m);
        }
    }

    public boolean hasSources() {
        return this.getSources() > 0;
    }

}
