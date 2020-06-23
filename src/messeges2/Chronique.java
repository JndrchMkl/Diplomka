package messeges2;

import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Chronique implements Runnable {
    boolean isRunning = true;
    HashMap<String, ConcurrentLinkedQueue<String>> chronique = new HashMap<>();

    @Override
    public void run() {
        while (isRunning) {

        }
    }

    void createChroniqueRecord(String record) {
        chronique.put(record, new ConcurrentLinkedQueue<>());
    }

    Queue<String> record(String entity) {
        return chronique.get(entity);
    }

}
