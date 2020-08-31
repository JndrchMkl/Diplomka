package cz.upa.simulation.messaging;

import cz.upa.simulation.domain.Settings;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadLocalRandom;

import static cz.upa.simulation.utils.StringUtils.s;

public class PostOffice implements Runnable {
    private ConcurrentHashMap<String, ConcurrentLinkedQueue<String[]>> postOffice;
    private Double score;
    private double timestampPresent;
    private double timeStopWatch;
    private double avgTalent;

    public PostOffice() {
        postOffice = new ConcurrentHashMap<>();
        avgTalent = (Settings.RANGE_TALENT_FROM + Settings.RANGE_TALENT_TO) / 2;
        score = 0.0;
        new Thread(this).start();
    }

    public void notifyTo(String owner, String... message) {
        Queue<String[]> inbox = inbox(owner);
        if (inbox != null) {
            inbox.add(message);
        }
    }

    public void notifyRandom(String... message) {
        int i = 0;

        int index = ThreadLocalRandom.current().nextInt(0, postOffice.keySet().size());

        for (String owner : postOffice.keySet()) {
            if (i == index) {
                Queue<String[]> inbox = inbox(owner);
                if (inbox == null) {
                    notifyRandom(message);
                } else {
                    inbox.add(message);
                    return;
                }
            }
            i++;
        }
    }

    public void notifyAll(String sender, String... message) {
        for (String owner : postOffice.keySet()) {
            if (!owner.equals(sender))
                notifyTo(owner, message);
        }
    }

    public void notifyEveryone(String... message) {
        for (String owner : postOffice.keySet()) {
            notifyTo(owner, message);
        }
    }

    public void createMailbox(String owner, Queue<String[]> inbox) {
        postOffice.put(owner, new ConcurrentLinkedQueue<>(inbox));
    }

    public void createMailbox(String owner) {
        postOffice.put(owner, new ConcurrentLinkedQueue<>());
    }

    public Queue<String[]> inbox(String owner) {
        return postOffice.get(owner);
    }

    public Queue<String[]> withdrawMessages(String owner) {
        Queue<String[]> messages = new LinkedList<>(inbox(owner));
        postOffice.put(owner, new ConcurrentLinkedQueue<>());
        return messages;
    }

    public void removeMailbox(String owner) {
        postOffice.remove(owner);
    }


    public synchronized void donate(double donation) {
        score += donation;
    }

    public Double getScore() {
        return score;
    }

    public ConcurrentHashMap<String, ConcurrentLinkedQueue<String[]>> getPostOffice() {
        return postOffice;
    }

    @Override
    public void run() {
        while (Settings.IS_SIMULATION_RUNNING) {
            double now = System.nanoTime();
            double interval = now - timestampPresent; // nutno pocitat s intervalem rovnym 0. POZOR!!! Nedělit s intervalem rovným 0!!!
            timestampPresent = now;
            timeStopWatch += interval;


            score += postOffice.size() * avgTalent * interval;

            if (timeStopWatch > 500000000.0) {
                notifyEveryone(MessageType.GIVE_SOURCE.value, s(score));
                score=0.0;
            }
        }
    }
}

