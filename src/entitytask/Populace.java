package entitytask;

import javafx.concurrent.Task;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

public class Populace {
    // nad ním je mutex a nepustí entitu drive nez operaci nad listem dokonci
    // ---: alternativa CopyOnWriteArrayList - vzuziva "Magie" a zamergovava postupne operace nad listem
    private final List<Entity> populace = Collections.synchronizedList(new LinkedList<>());
    private final ExecutorService threadPool = Executors.newFixedThreadPool(4);
    private final List<List<Entity>> historie = new LinkedList<>();

    public void addEntity(Entity e) {
        populace.add(e);
    }

    public void addAllEntities(Collection<? extends Entity> entities) {
        populace.addAll(entities);
    }

    public int size() {
        return populace.size();
    }

    public Entity findFittest() {
        PriorityQueue<Entity> entityPriorityQueue = new PriorityQueue<>(populace);
        Entity fittest, ent;
        while (!entityPriorityQueue.isEmpty()) {
            ent = entityPriorityQueue.poll();
            if (ent.getSources() > 2000) {
                fittest = ent;
                return fittest;
            }
        }
        return null;
    }

    public void nextTick() {
        this.historie.add(clonePopulation());
        System.out.println("History has been logged...");
        try {
            Function<Task<Entity>, Callable<Entity>> taskWrapper = task -> () -> {
                System.out.println(task.toString());
                task.run();
                return task.getValue();
            };
            List<Future<Entity>> results = this.threadPool.invokeAll(populace.stream()
                    .map(Entity::createTask)
                    .map(taskWrapper)
                    .collect(toList()));
            System.out.println("Results count: " + results.size());
            System.out.println("Results: " + results.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private List<Entity> clonePopulation() {
        return this.populace.stream().map(Entity::new).collect(toList());
    }

}
