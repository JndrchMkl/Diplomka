package messeges2;

import entitytask.Entity;
import entitytask.Population;
import org.w3c.dom.ls.LSOutput;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    public static LinkedList<Entita> generate(PostOffice postOffice, int size) {
        LinkedList<Entita> entityList = new LinkedList<>();


        for (int i = 0; i < size; i++) {
//            Double sources = ThreadLocalRandom.current().nextDouble(0.0, SystemCore.CHILD_EXPENSE); // starting sources
            Double talent = ThreadLocalRandom.current().nextDouble(1.0, 10.0 + 1);
            entityList.add(new Entita(postOffice, 0.0, talent));
        }
        return entityList;
    }

    public static void main(String[] args) {
        int x = 4;
        PostOffice postOffice = new PostOffice();
        LinkedList<Entita> initList = generate(postOffice, 2);
        System.out.println("Starting!");
        for (Entita e : initList) {
            e.thread().start();
        }

        System.out.println("Done!");
    }
}