package messeges2;

import entitytask.Entity;
import entitytask.Population;
import org.w3c.dom.ls.LSOutput;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    public static LinkedList<Entita> generate(PostOffice postOffice, Matrika matrika, int size) {
        LinkedList<Entita> entityList = new LinkedList<>();


        for (int i = 0; i < size; i++) {
//            Double sources = ThreadLocalRandom.current().nextDouble(0.0, SystemCore.CHILD_EXPENSE); // starting sources
            Double talent = ThreadLocalRandom.current().nextDouble(1.0, 10.0 + 1);
            entityList.add(new Entita(matrika, postOffice, 0.0, talent));
        }
        return entityList;
    }

    public static void main(String[] args) {
        PostOffice postOffice = new PostOffice();
        Matrika matrika = new Matrika();
        LinkedList<Entita> initList = generate(postOffice, matrika, 2);

    }
}
