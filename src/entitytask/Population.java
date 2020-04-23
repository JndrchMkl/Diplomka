package entitytask;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static entitytask.SystemCore.CHILD_EXPENSE;

public class Population {
    // nad ním je mutex a nepustí entitu drive nez operaci nad listem dokonci
    // ---: alternativa CopyOnWriteArrayList - vzuziva "Magie" a zamergovava postupne operace nad listem
    private final List<Entity> actualPopulation = Collections.synchronizedList(new LinkedList<>());
    private final ExecutorService threadPool = Executors.newFixedThreadPool(SystemCore.N_THREADS);

    private List<Entity> reproducableEntities;

    public void addEntity(Entity e) {
        actualPopulation.add(e);
    }

    public void addAllEntities(Collection<? extends Entity> entities) {
        actualPopulation.addAll(entities);
    }

    public int size() {
        return actualPopulation.size();
    }

    public synchronized Entity findFittest(Entity requesting) {
        if (requesting.getSources()<CHILD_EXPENSE)
            return null;
        Entity bestPartner=null;
        Collections.sort(reproducableEntities);
        for (Entity e :reproducableEntities) {
            if (e.getSources()>CHILD_EXPENSE&&!Objects.equals(e, requesting)) {
                bestPartner = e;
                break;
            }
        }
        if (bestPartner != null) {
            reproducableEntities.remove(requesting);
            reproducableEntities.remove(bestPartner);
        }
        return bestPartner;
    }

    public void nextTick() {
        try {
            reproducableEntities = new LinkedList<>(actualPopulation);
            List<Future<Entity>> allTickFutures = new ArrayList<>();
            for (Entity entity : actualPopulation) {
                Future<Entity> future = threadPool.submit(entity);
                entity.setEntityFuture(future);
                allTickFutures.add(future);
            }

            // Wait until every single genetics.Entity call() method has been done.
            waitForPromises(allTickFutures);

            for (Entity entity : new LinkedList<>(actualPopulation)) {
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

    public List<Entity> state() {
        return this.actualPopulation;
    }


}
