package cz.upa.simulation.domain;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Matrika implements Runnable {
    boolean isRunning = true;
    HashMap<String, ConcurrentLinkedQueue<String[]>> records = new HashMap<>();

    HashMap<String, ConcurrentLinkedQueue<String[]>> records() {
        return records;
    }

    public int nRecords() {
//        int total=0;
//        for (ConcurrentLinkedQueue<String[]> l : records().values()) {
//            total += l.size();
//        }
        return records.size();
    }

    void oznamPodatelne(String owner, String[] message) {
        ConcurrentLinkedQueue<String[]> recordsAboutOwner = recordAbout(owner);
        if (recordsAboutOwner != null)
            recordsAboutOwner.add(message);

    }

    void createNewRecord(String owner) {
        records.put(owner, new ConcurrentLinkedQueue<>());
    }

    ConcurrentLinkedQueue<String[]> recordAbout(String entity) {
        return records.get(entity);

    }

    @Override
    public void run() {
//        while (isRunning) {
//
//            for (String key : records.keySet()) {
//                ConcurrentLinkedQueue<String[]> records = this.records.get(key);
//                while (!records.isEmpty()) {
//                    String[] message = records.poll();
//
//                    if (message[0].equals(WE_HAVE_A_BABY.value)) {
//                        // zaznamenej novy prirustek do matriky
//
//                    }
//                    if (message[0].equals(I_DIED.value)) {
//                        // zaznamenej smrt do matriky
//
//                    }
//                }
//            }
//
//        }
    }

    public void removeRecord(String name) {
        records.remove(name);
    }
}
