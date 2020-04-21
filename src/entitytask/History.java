package entitytask;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.toList;

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
        return this.population.state().stream().map(Entity::new).collect(toList());
    }
}
