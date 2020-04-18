package entitytask;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.util.stream.Collectors.toList;

public class Population {
    // nad ním je mutex a nepustí entitu drive nez operaci nad listem dokonci
    // ---: alternativa CopyOnWriteArrayList - vzuziva "Magie" a zamergovava postupne operace nad listem
    private final List<Entity> population = Collections.synchronizedList(new LinkedList<>());
    private final ExecutorService threadPool = Executors.newFixedThreadPool(SystemCore.N_THREADS);

    private List<Entity> reproducableEntities;

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
        Entity bestPartner = reproducableEntities.stream().sorted()
                .filter(e -> !Objects.equals(e.getName(), requesting.getName()))
                .findFirst().orElse(null);
        if (bestPartner != null) {
            reproducableEntities.remove(requesting);
            reproducableEntities.remove(bestPartner);
        }
        return bestPartner;
    }

    public void nextTick() {
        try {
            reproducableEntities = new LinkedList<>(population);
            List<Future<Entity>> allTickFutures = new ArrayList<>();
            for (Entity entity : population) {
                Future<Entity> future = threadPool.submit(entity);
                entity.setEntityFuture(future);
                allTickFutures.add(future);
            }

            // Wait until every single genetics.Entity call() method has been done.
            waitForPromises(allTickFutures);

            for (Entity entity : new LinkedList<>(population)) {
                entity.reproduce();
                entity.setBestPartner(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void waitForPromises(List<Future<Entity>> allTickFutures) throws InterruptedException, java.util.concurrent.ExecutionException {
        //neskoncime nextTick(), dokud nejsou dokonceny Future pro kazdou entitu
        //funguje jako bariera
        for (Future f : allTickFutures) {
            f.get();
        }
    }

    private List<Entity> clonePopulation() {
        return this.population.stream().map(Entity::new).collect(toList());
    }

}
