package messeges2;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class PostOffice {
    HashMap<String, Queue<String>> postOffice = new HashMap<>();

    void notifyTo(String owner, String message) {
        inbox(owner).add(message);
    }

    void notifyAll(String message) {
        for (String owner : postOffice.keySet()) {
            inbox(owner).add(message);
        }
    }

    void createMailbox(String owner) {
        postOffice.put(owner, new LinkedList<>());
    }

    Queue<String> inbox(String owner) {
        return postOffice.get(owner);
    }


}
