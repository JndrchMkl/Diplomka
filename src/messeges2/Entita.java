package messeges2;


import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ThreadLocalRandom;

import static messeges2.MessageType.*;
import static messeges2.TimeUtils.systemNanoTime;

public class Entita implements Runnable {

    // Character attributes
    private double patience;
    private double strength;
    private double perception;

    private double sources;
    private double talent;
    private float timestampPresent;
    private float timestampEnd;
    private float timeout;
    private PostOffice postOffice;
    private Queue<String> messages;
    private Queue<String> potentialPartners;
    private boolean isAlive;
    private boolean intentLookForYourNewPartner;
    private boolean intentDecideWhoIsPartnerRightNow;
    private boolean intentSteal;
    private boolean intentMurder;
    private Thread thread;

    protected final int CHILD_EXPENSE = 100;
    protected final String COMMA = ",";

    public Entita(PostOffice postOffice, double sources, double talent) {
        this.sources = sources;
        this.talent = talent;
        this.postOffice = postOffice;
        this.timestampPresent = systemNanoTime(); //actual time zero
        this.timestampEnd = timestampPresent + timestampPresent;
        this.isAlive = true;
        this.intentLookForYourNewPartner = false;
        this.intentDecideWhoIsPartnerRightNow = false;
        this.intentSteal = false;
        this.intentMurder = false;
        patience = ThreadLocalRandom.current().nextDouble(1.0, 10.0);
        strength = ThreadLocalRandom.current().nextDouble(1.0, 10.0);
        perception = ThreadLocalRandom.current().nextDouble(1.0, 10.0);
        messages = new LinkedList<>();
        potentialPartners = new LinkedList<>();
        thread = new Thread(this);
        postOffice.createMailbox(name());
//        Thread thread = new Thread(this);
//        thread.start();
    }

    @Override
    public void run() {
        System.out.println("Hello I am new " + name());
        while (isAlive) {
            /// Phase 1 - upkeep
            // timestamps
            float now = systemNanoTime();
            float interval = now - timestampPresent; // nutno pocitat s intervalem rovnym 0. POZOR!!! Nedělit s intervalem rovným 0!!!
            System.out.println(now);
            timestampPresent = now;

            // zemri starim nebo hladověním
            if (timestampPresent > timestampEnd) {
                die();
                return;
            }

            // income
            messages.addAll(postOffice.withdraw(name()));
            salary(interval);
            liveCosts(interval);

            // Intents
            // Process the intent
            if (intentLookForYourNewPartner) { // hledam partnera
                // option 1 - notify every single entity
                // option 2 - notify all entities, but tell them requirements on parameters - NOVA ZPRAVA LOOKING_FOR_PARTNER_WITH_PARAMETERS
                // option 3 - notify entity directly - našel jsem si partnera a už nechci žádného jiného...
                postOffice.notifyAll(name(), LOOKING_FOR_PARTNER.value + COMMA + talent + COMMA + sources + COMMA + name());

                // set time stamp, when entity will must decide
                timeout = (float) (timestampPresent + 1000000.0 * patience);
            }
            if (intentDecideWhoIsPartnerRightNow) {
                //pokud vyprsel cas (timeout) pro hledani partnera, dojdu k rozhodnuti a oznamim mu ze mam s nim dite
                // candidate of having baby
                // option 1 - accept first
                // option 2 - accept best by some criterium talent, age, strength, patience
                String mostTalented = "0";
                String[] best = null;
                for (String pp : potentialPartners) {
                    String[] data = pp.split(COMMA);
                    if (mostTalented.compareTo(data[0]) > 0) {
                        mostTalented = data[0];
                        best = data;
                    }
                }
                potentialPartners.clear();
                if (best != null) {
                    sources = sources - CHILD_EXPENSE;
                    Entita child = new Entita(postOffice, 0, (talent + Double.parseDouble(best[1])) / 2);
                    child.thread().start();
                    postOffice.notifyTo(best[2], YOU_ARE_DAD.value + COMMA + talent + COMMA + sources + COMMA + name());
                }
            }
            if (intentSteal) {
                postOffice.notifyRandom(WANT_STEAL.value + COMMA + name() + COMMA + perception);
            }
            if (intentMurder) {
                postOffice.notifyRandom(WANT_KILL.value + COMMA + name() + COMMA + strength);
            }


            // Decide about your intent / Choose your intent
            intentLookForYourNewPartner = sources > CHILD_EXPENSE;
            intentDecideWhoIsPartnerRightNow = timestampPresent > timeout;
//            intentSteal = CHILD_EXPENSE > timestampPresent+interval*100 (income(interval) * 100);


            /// Phase 2 - process messages
            while (!messages.isEmpty()) {
                String message = messages.poll();//read and del
                String[] data = message.split(COMMA); // TODO null pointer

                if (data[0].equals(LOOKING_FOR_PARTNER.value)) {
                    //odpovidam yes partner (nebo mlcim a ignoruji)
                    postOffice.notifyTo(data[3], YES_PARTNER.value + COMMA + talent + COMMA + sources + COMMA + name());
                }
                if (data[0].equals(YES_PARTNER.value)) {
                    potentialPartners.add(talent + COMMA + sources + COMMA + name());
                }
                if (data[0].equals(YOU_ARE_DAD.value)) {
                    if (sources > CHILD_EXPENSE) {
                        sources -= CHILD_EXPENSE;
                        postOffice.notifyTo(data[3], GIVE_SOURCE.value + COMMA + CHILD_EXPENSE);
                    }
                }
                if (data[0].equals(GIVE_SOURCE.value)) {
                    double gift = Double.parseDouble(data[1]);
                    sources = sources + gift;
                }
                if (data[0].equals(WANT_STEAL.value)) {
                    if (perception < Float.parseFloat(data[2])) {
                        postOffice.notifyTo(data[1], GIVE_SOURCE.value + COMMA + sources);
                        sources = 0;
                    }
                }
                if (data[0].contains(WANT_KILL.value)) {
                    if (strength < Float.parseFloat(data[2])) {
                        postOffice.notifyTo(data[1], GIVE_SOURCE.value + COMMA + sources);
                        sources = 0;
                        this.die();
                    }

                }
            }


        }
    }

    private String name() {
        return thread.getName();
    }

    void salary(float timeInterval) {
        sources = sources + income(timeInterval);
    }

    private double income(float timeInterval) {
        return (1 * talent) * timeInterval;
    }

    void liveCosts(float timeInterval) {
        sources = sources - (1 * timeInterval);

    }

    void die() {
        postOffice.removeMailbox(name());
        isAlive = false;
    }

    Thread thread() {
        return thread;
    }
}
