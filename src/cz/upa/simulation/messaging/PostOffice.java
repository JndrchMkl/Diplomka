package cz.upa.simulation.messaging;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadLocalRandom;

public class PostOffice {
    ConcurrentHashMap<String, ConcurrentLinkedQueue<String[]>> postOffice = new ConcurrentHashMap<>();

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
                if (inbox(owner) == null) {
                    notifyRandom(message);
                }else {
                    inbox(owner).add(message);
                }
                return;
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

}
