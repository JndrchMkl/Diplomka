package entitytask;

//import javafx.concurrent.Task;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.util.stream.Collectors.toList;

public class Population {
    // nad ním je mutex a nepustí entitu drive nez operaci nad listem dokonci
    // ---: alternativa CopyOnWriteArrayList - vzuziva "Magie" a zamergovava postupne operace nad listem
    private final List<Entity> population = Collections.synchronizedList(new LinkedList<>());
    private final ExecutorService threadPool = Executors.newFixedThreadPool(4);
    private final List<List<Entity>> history = Collections.synchronizedList(new LinkedList<>());
    private List<Entity> entities;

    public void addEntity(Entity e) {
        population.add(e);
    }

    public void addAllEntities(Collection<? extends Entity> entities) {
        population.addAll(entities);
    }

    public int size() {
        return population.size();
    }

    public synchronized Entity findFittest(Entity requesting) {
        return entities.stream().sorted().filter(e -> !Objects.equals(e.getName(), requesting.getName())).findFirst().orElseGet(() -> null);
    }

    public void nextTick() {
        entities = new LinkedList<>(population);
        this.history.add(clonePopulation());
        System.out.println("History has been logged...");
        try {
            List<Future> allTickFutures = new ArrayList<Future>();
            for (Entity entity : population) {
                Future<Entity> future = threadPool.submit(entity);
                entity.setEntityFuture(future);
                allTickFutures.add(future);
            }
            //neskoncime nextTick(), dokud nejsou dokonceny Future pro kazdou entitu
            for (Future f : allTickFutures) {
                f.get();
            }
            System.out.println("Results count: " + allTickFutures.size());
            System.out.println("Results: " + Arrays.toString(allTickFutures.toArray()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Entity> clonePopulation() {
        return this.population.stream().map(Entity::new).collect(toList());
    }

}
