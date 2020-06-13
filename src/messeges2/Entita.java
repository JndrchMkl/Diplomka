package messeges2;


import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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

    public Entita(PostOffice postOffice, double sources, double talent) {
        this.sources = sources;
        this.talent = talent;
        this.postOffice = postOffice;
        this.timestampPresent = systemNanoTime();
        this.timestampEnd = timestampPresent + 50000;
        this.isAlive = false;
        this.intentWantChild = false;
        messages = new LinkedList<>();
        postOffice.createMailbox(this.toString());
//        Thread thread = new Thread(this);
//        thread.start();
    }

    @Override
    public void run() {
        System.out.println("Hello " + this.toString());
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
            messages.addAll(postOffice.inbox(this.toString()));
            salary(interval);
            liveCosts(interval);

            // Choose your intent
            intentWantChild = sources > 10;
            // TODO


            // Phase 2 - process messages
            String message = messages.poll();//del
            if (intentWantChild) {
                //1 harvest accept baby from others
                //1 pribalim s acceptem prachy, pri prijmuti odectu z this prachy
                //2 harvest request from others
                //2 choose and send accept
                //3 send request to others


//                    LinkedList<String> partnerCandidateMessages = messages.stream().map(m c m.contains("chciDite"))

                for (String messege : messages) {
                    if (messege.contains("chciDite")) {
//                        partnerCandidateMessages.add(messege);
                    }

                }
                postOffice.notifyAll(this.toString(), "chciDite," + talent + "," + sources);
            }
            if (message.contains("chciDite")) {
                postOffice.notifyAll(this.toString(), "chciDite," + talent + "," + sources);
            }
        }
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
        return new Thread(this);
    }
}
