package messeges;


import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class History {
    private final List<List<Entity>> history = Collections.synchronizedList(new LinkedList<>());
    private final Population population;

    public History(Population population) {
        this.population = population;
    }

    /**
     * Save actual state of Population
     */
    public void save() {
        history.add(clonePopulation());
    }

    /**
     * Add one part of history.
     *
     * @param historyBatch one tick of history
     */
    public void add(LinkedList<Entity> historyBatch) {
        history.add(historyBatch);
    }

    public List<List<Entity>> getHistory() {
        return this.history;
    }

    private List<Entity> clonePopulation() {
        LinkedList<Entity> clonedList = new LinkedList<>();
        for (Entity e : population.state()) {
            Entity clone = new Entity(e);
//            Entity clone =  e.clone();
//            Entity clone = SerializationUtils.clone(e);
            clonedList.add(clone);
        }
        return clonedList;
    }
}
