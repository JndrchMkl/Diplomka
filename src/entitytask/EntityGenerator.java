package entitytask;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.concurrent.ThreadLocalRandom;

public class EntityGenerator {
   private static int counter = 0;

    public static LinkedList<Entity> generate(Population population, int size) {
        LinkedList<Entity> entityList = new LinkedList<>();
        for (int i = 0; i < size; i++) {
//            Double sources = ThreadLocalRandom.current().nextDouble(0.0, SystemCore.CHILD_EXPENSE); // starting sources
            Double talent = ThreadLocalRandom.current().nextDouble(1.0, 10.0 + 1);
            entityList.add(new Entity(population, "E" + (counter++), talent));
        }
        return entityList;
    }


}
