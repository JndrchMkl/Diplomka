package messeges2;


import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static messeges2.MessageType.*;
import static messeges2.TimeUtils.systemNanoTime;

public class Entita implements Runnable {
    private double sources;
    private double talent;
    private float timestampPresent;
    private float timestampEnd;
    private PostOffice postOffice;
    private boolean isAlive = true;
    private Queue<String> messages;
    private boolean intentWantChild;
    private String COMMA = ",";
    ;
    private Thread thread;
    private int CHILD_EXPENSE = 10;

    public Entita(PostOffice postOffice, double sources, double talent) {
        this.sources = sources;
        this.talent = talent;
        this.postOffice = postOffice;
        this.timestampPresent = systemNanoTime();
        this.timestampEnd = timestampPresent + 500000000;
        this.isAlive = true;
        this.intentWantChild = false;
        messages = new LinkedList<>();
        thread = new Thread(this);
        postOffice.createMailbox(name());
//        Thread thread = new Thread(this);
//        thread.start();
    }

    @Override
    public void run() {
        System.out.println("Hello " + name());
        while (isAlive) {
            // Phase 1 - upkeep
            // timestamps
            float now = systemNanoTime();
            float interval = now - timestampPresent;
            timestampPresent = now;

            if (timestampPresent > timestampEnd) {
                die();
                return;
            }

            // vyzvedni postu
            messages.addAll(postOffice.inbox(name()));
            salary(interval);
            liveCosts(interval);

            // Choose your intent
            intentWantChild = sources > CHILD_EXPENSE;
            // TODO
            if (intentWantChild) {
                //1 harvest accept baby from others
                //1 pribalim s acceptem prachy, pri prijmuti odectu z this prachy
                //2 harvest request from others
                //2 choose and send accept
                //3 send request to others


//                    LinkedList<String> partnerCandidateMessages = messages.stream().map(m c m.contains("chciDite"))

//                for (String messege : messages) {
//                    if (messege.contains("chciDite")) {
////                        partnerCandidateMessages.add(messege);
//                    }
//
//                }
                postOffice.notifyAll(name(), WANT_BABY.value + COMMA + talent + COMMA + sources + COMMA + name());
            }


            // Phase 2 - process messages
            System.out.println(name() + " processing messages...");

            while (!messages.isEmpty()) {
                String message = messages.poll();//read and del
                String[] data = message.split(COMMA);

                if (message.contains(WANT_BABY.value)) {
                    // candidate of having baby
                    // option 1 - accept first
                    // option 2 - accept best

                    double partnerParent = Double.parseDouble(data[1]);
                    postOffice.notifyTo(data[2], ACCEPT_BABY.value + COMMA + talent + COMMA + sources);
                    sources = sources - CHILD_EXPENSE;
                }
                if (message.contains(ACCEPT_BABY.value)) {

                    double partnerParent = Double.parseDouble(data[1]);
                    this.sources = sources - CHILD_EXPENSE;
                    Entita child = new Entita(postOffice, 0, (this.talent + partnerParent) / 2);
                    child.thread().start();
                }
                if (message.contains(GIVE_SOURCE.value)) {

                    double gift = Double.parseDouble(data[1]);
                    sources = sources + gift;
                }
                if (message.contains(WANT_STEAL.value)) {

                    postOffice.notifyTo(data[1], GIVE_SOURCE.value + COMMA + sources);
                    sources = 0;
                }
                if (message.contains(WANT_KILL.value)) {

                    postOffice.notifyTo(data[1], GIVE_SOURCE.value + COMMA + sources);
                    this.die();
                }
            }


        }
    }

    private String name() {
        return thread.getName();
    }

    void salary(float timeInterval) {
        sources = (sources + (1 * talent)) * timeInterval;
    }

    void liveCosts(float timeInterval) {
        sources = (sources - 1) * timeInterval;

    }

    void die() {
        isAlive = false;
    }

    Thread thread() {
        return thread;
    }
}
