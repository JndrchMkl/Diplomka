package messeges2;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static messeges2.MessageType.I_DIED;
import static messeges2.MessageType.WE_HAVE_A_BABY;

public class Matrika implements Runnable {
    boolean isRunning = true;
    HashMap<String, ConcurrentLinkedQueue<String[]>> podatelna = new HashMap<>();
    LinkedList<String[]> matrika = new LinkedList<>();

    void oznamPodatelne(String jednatel, String[] message) {
        recordAbout(jednatel).add(message);

    }

    void createChroniqueRecord(String record) {
        podatelna.put(record, new ConcurrentLinkedQueue<>());
    }

    ConcurrentLinkedQueue<String[]> recordAbout(String entity) {
        return podatelna.get(entity);

    }

    @Override
    public void run() {
        while (isRunning) {

            for (String key : podatelna.keySet()) {
                ConcurrentLinkedQueue<String[]> records = podatelna.get(key);
                while (!records.isEmpty()) {
                    String[] message = records.poll();

                    if (message[0].equals(WE_HAVE_A_BABY.value)) {
                        // zaznamenej novy prirustek do matriky

                    }
                    if (message[0].equals(I_DIED.value)) {
                        // zaznamenej smrt do matriky

                    }
                }
            }

        }
    }
}
