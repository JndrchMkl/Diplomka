package messeges2;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadLocalRandom;

public class PostOffice {
    HashMap<String, ConcurrentLinkedQueue<String>> postOffice = new HashMap<>();

    void notifyTo(String owner, String message) {
        inbox(owner).add(message);
    }

    void notifyRandom(String message) {
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

    void notifyAll(String sender, String message) {
        for (String owner : postOffice.keySet()) {
            if (!owner.equals(sender))
                notifyTo(owner, message);
        }
    }

    void createMailbox(String owner) {
        postOffice.put(owner, new ConcurrentLinkedQueue<>());
    }

    Queue<String> inbox(String owner) {
        return postOffice.get(owner);
    }

    Queue<String> withdraw(String owner) {
        Queue<String> messages = inbox(owner);
        inbox(owner).clear();
        return postOffice.get(owner);
    }

    void removeMailbox(String owner) {
        postOffice.remove(owner);
    }

}
