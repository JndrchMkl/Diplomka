package cz.upa.simulation;

import cz.upa.simulation.domain.Entita;
import cz.upa.simulation.domain.Matrika;
import cz.upa.simulation.messaging.PostOffice;
import cz.upa.simulation.messaging.Societies;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    public static LinkedList<Entita> generate(PostOffice postOffice, Societies societies, int size) {
        LinkedList<Entita> entityList = new LinkedList<>();


        for (int i = 0; i < size; i++) {
//            Double sources = ThreadLocalRandom.current().nextDouble(0.0, SystemCore.CHILD_EXPENSE); // starting sources
            Double talent = ThreadLocalRandom.current().nextDouble(5.0, 10.0 + 1);
            entityList.add(new Entita(societies, postOffice, 0.0, talent,"",""));
        }
        return entityList;
    }

    public static void main(String[] args) {
        PostOffice postOffice = new PostOffice();
        Societies societies = new Societies();
        LinkedList<Entita> initList = generate(postOffice, societies, 2);

    }
}
