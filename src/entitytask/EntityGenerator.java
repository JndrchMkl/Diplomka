package entitytask;

import java.util.PriorityQueue;
import java.util.concurrent.ThreadLocalRandom;

public class EntityGenerator {

    public static PriorityQueue<Entity> generate(Population population, int size) {
        PriorityQueue<Entity> entityList = new PriorityQueue<>();
        for (int i = 0; i < size; i++) {
//            Double sources = ThreadLocalRandom.current().nextDouble(0.0, SystemCore.CHILD_EXPENSE); // starting sources
            Double talent = ThreadLocalRandom.current().nextDouble(10.0, 10.0 + 1);
            entityList.add(new Entity(population, "E" + i, talent));
        }
        return entityList;
    }


}
