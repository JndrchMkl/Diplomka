package cz.upa.simulation.messaging;

import cz.upa.simulation.domain.Settings;
import cz.upa.simulation.messaging.MessageType;
import cz.upa.simulation.messaging.PostOffice;
import cz.upa.simulation.messaging.Societies;

import java.util.Iterator;
import java.util.List;

import static cz.upa.simulation.utils.StringUtils.s;

public class SocietyGarbageCollector implements Runnable{
    Societies societies;

    public SocietyGarbageCollector(Societies societies) {
        this.societies = societies;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (Settings.IS_SIMULATION_RUNNING) {
            List<PostOffice> postOffices = societies.getPostOffices();
            postOffices.removeIf(p -> p.getPostOffice().size() == 0);
            Iterator<PostOffice> iterator = postOffices.iterator();
            while (iterator.hasNext()) {
                PostOffice p = iterator.next();
                if (p.getPostOffice().size()==0){
                    iterator.remove();
                }
            }
        }
    }
}
