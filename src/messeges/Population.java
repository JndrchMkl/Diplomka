package messeges;


import java.util.*;

import static entitytask.SystemCore.CHILD_EXPENSE;

public class Population {
    // nad ním je mutex a nepustí entitu drive nez operaci nad listem dokonci
    // ---: alternativa CopyOnWriteArrayList - vzuziva "Magie" a zamergovava postupne operace nad listem
    private final List<Entity> actualPopulation = Collections.synchronizedList(new LinkedList<>());


    public Entity findFittest(Entity requesting) {
        if (requesting.getSources() < CHILD_EXPENSE)
            return null;
        if (actualPopulation.size() < 1)
            return null;

        Entity bestPartner = actualPopulation.get(0);
        for (Entity e : actualPopulation) {
            if (e.getSources() > CHILD_EXPENSE &&
                    (Objects.compare(e, bestPartner, Entity::compareTo) > 0) &&
                    !Objects.equals(e, requesting)) {
                bestPartner = e;
            }
        }

        // In the case when no one has sources even Entity on 0 index
        if (Objects.equals(bestPartner, requesting))
            return null;

        return bestPartner;
    }


    public List<Entity> state() {
        return this.actualPopulation;
    }


    public void addEntity(Entity e) {
        actualPopulation.add(e);
    }

    public void addAllEntities(Collection<? extends Entity> entities) {
        actualPopulation.addAll(entities);
    }
}
