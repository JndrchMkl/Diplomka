package messeges2;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadLocalRandom;

public class PostOffice {
    ConcurrentHashMap<String, ConcurrentLinkedQueue<String[]>> postOffice = new ConcurrentHashMap<>();

    void notifyTo(String owner, String[] message) {
        Queue<String[]> inbox = inbox(owner);
        if (inbox != null) {
            inbox(owner).add(message);
        }
    }

    void notifyRandom(String[] message) {
        int i = 0;
        int index = ThreadLocalRandom.current().nextInt(0, postOffice.keySet().size() - 1);

        for (String owner : postOffice.keySet()) {
            if (i == index) {
                inbox(owner).add(message);
                return;
            }
            i++;
        }
    }

    void notifyAll(String sender, String[] message) {
        for (String owner : postOffice.keySet()) {
            if (!owner.equals(sender))
                notifyTo(owner, message);
        }
    }

    void createMailbox(String owner) {
        postOffice.put(owner, new ConcurrentLinkedQueue<>());
    }

    Queue<String[]> inbox(String owner) {
        return postOffice.get(owner);
    }

    Queue<String[]> withdrawMessages(String owner) {
        Queue<String[]> messages = new LinkedList<>(inbox(owner));
        postOffice.put(owner, new ConcurrentLinkedQueue<>());
        return messages;
    }

    void removeMailbox(String owner) {
        postOffice.remove(owner);
    }

}
