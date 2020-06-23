package messeges2;


import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
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
    private double timestampPresent;
    private double timestampEnd;
    private double timeout;
    private PostOffice postOffice;
    private Queue<String> messages;
    private Queue<String> potentialPartners;
    private List<Double> intervalList;
    private boolean isAlive;
    private boolean intentLookForYourNewPartner;
    private boolean intentDecideWhoIsPartnerRightNow;
    private boolean intentSteal;
    private boolean intentMurder;
    private Thread thread;
    private int nTicks = 0;

    //    protected final int CHILD_EXPENSE = 1000000;
    protected final int CHILD_EXPENSE = 1;
    protected final String COMMA = ",";
    private double timeStopWatch = 0;

    public Entita(PostOffice postOffice, double sources, double talent) {
        this.sources = sources;
        this.talent = talent;
        this.postOffice = postOffice;
        this.timestampPresent = timeNow(); //actual time zero
//        this.timestampEnd = timestampPresent +(2* ThreadLocalRandom.current().nextDouble(5.0, 25.0));
        this.timestampEnd = timestampPresent * 2;
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
        intervalList = new LinkedList<>();
        thread = new Thread(this);
        postOffice.createMailbox(name());
    }

    private double calculateAverage(List<Double> marks) {
        Double sum = 0.0;
        if (!marks.isEmpty()) {
            for (Double mark : marks) {
                sum += mark;
            }
            return sum / nTicks;
        }
        return sum;
    }

    @Override
    public void run() {
        System.out.println("Hello I am new " + name() + ", " + talent + ", " + patience);
        while (isAlive) {
            /// Phase 1 - upkeep
            // timestamps
            double now = timeNow();
            double interval = now - timestampPresent; // nutno pocitat s intervalem rovnym 0. POZOR!!! Nedělit s intervalem rovným 0!!!
            timestampPresent = now;
            timeStopWatch += interval;
            intervalList.add(income(interval));
            nTicks++;


            // zemri starim (nebo hladověním)
            if (timestampPresent > timestampEnd) {
                die();
                postOffice.removeMailbox(name());
                return;
            }

            // income
            messages.addAll(postOffice.withdrawMessages(name()));
            salary(interval);
            liveCosts(interval);

            // Intents
            // 01 - Process the intent
            if (intentLookForYourNewPartner) { // hledam partnera
                // option 1 - notify every single entity
                // option 2 - notify all entities, but tell them requirements on parameters - NOVA ZPRAVA LOOKING_FOR_PARTNER_WITH_PARAMETERS
                // option 3 - notify entity directly - našel jsem si partnera a už nechci žádného jiného...
                postOffice.notifyAll(name(), LOOKING_FOR_PARTNER.value + COMMA + talent + COMMA + sources + COMMA + name());
//                System.out.println(name() + " notifying all - LOOKING_FOR_PARTNER");

                if (timeout == 0) {
                    // set time stamp, when entity will must decide
                    timeout = (calculateAverage(intervalList) * patience);
                }

            }
            // 02 - Process the intent
            if (intentDecideWhoIsPartnerRightNow) {
                //pokud vyprsel cas (timeout) pro hledani partnera, dojdu k rozhodnuti a oznamim mu ze mam s nim dite
                // candidate of having baby
                // option 1 - accept first
                // option 2 - accept best by some criterium talent, age, strength, patience
                Double mostTalented = 0.0;
                String[] best = null;

//                System.out.println(name() + " - TIMEOUT");
                for (String pp : potentialPartners) {
                    String[] data = pp.split(COMMA);
                    Double talent = Double.parseDouble(data[0]);
                    if (talent.compareTo(mostTalented) > 0) { // TODO faster way to compare talents, without parsing
                        mostTalented = talent;
                        best = data;
                    }
                }
                if (best != null) {
//                    System.out.println(name() + " - TIMEOUT - found " + best[2]);
                    sources = sources - CHILD_EXPENSE;
                    Entita child = new Entita(postOffice, 0, (talent + Double.parseDouble(best[1])) / 2);
                    child.thread().start();
                    postOffice.notifyTo(best[2], YOU_ARE_DAD.value + COMMA + talent + COMMA + sources + COMMA + name());
                    timeStopWatch = 0;
                    potentialPartners.clear();
                }
            }
            // 03 - Process the intent
//            if (intentSteal) {
//                postOffice.notifyRandom(WANT_STEAL.value + COMMA + name() + COMMA + perception);
//            }
//            // 04 - Process the intent
//            if (intentMurder) {
//                postOffice.notifyRandom(WANT_KILL.value + COMMA + name() + COMMA + strength);
//            }


            // Decide about your intent / Choose your intent
            intentLookForYourNewPartner = sources > CHILD_EXPENSE;
            intentDecideWhoIsPartnerRightNow = timeStopWatch > timeout;
//            intentSteal = timeStopWatch > 15;
//            intentMurder = timeStopWatch > 20;


            /// Phase 2 - process messages
            while (!messages.isEmpty()) {
                String message = messages.poll();//read and del
                String[] data = message.split(COMMA); // TODO null pointer

                if (data[0].equals(LOOKING_FOR_PARTNER.value)) {
                    //odpovidam yes partner (nebo mlcim a ignoruji)
                    postOffice.notifyTo(data[3], YES_PARTNER.value + COMMA + talent + COMMA + sources + COMMA + name());
//                    System.out.println(name() + " Processing - LOOKING_FOR_PARTNER");
                }
                if (data[0].equals(YES_PARTNER.value)) {
                    potentialPartners.add(data[1] + COMMA + data[2] + COMMA + data[3]);
//                    System.out.println(" Processing - YES_PARTNER : Entity " + name() + " accepting " + data[3] + " as a partner...");
                }
                if (data[0].equals(YOU_ARE_DAD.value)) {
                    if (sources > CHILD_EXPENSE) {
                        sources -= CHILD_EXPENSE;
                        postOffice.notifyTo(data[3], GIVE_SOURCE.value + COMMA + CHILD_EXPENSE);
                    }
                    System.out.println(name() + " Processing - YOU_ARE_DAD");
                }
                if (data[0].equals(GIVE_SOURCE.value)) {
                    double gift = Double.parseDouble(data[1]);
                    sources = sources + gift;
                    System.out.println(name() + " Processing - GIVE_SOURCE");
                }
//                if (data[0].equals(WANT_STEAL.value)) {
//                    if (perception < Float.parseFloat(data[2])) {
//                        postOffice.notifyTo(data[1], GIVE_SOURCE.value + COMMA + sources);
//                        sources = 0;
//                    }
//                }
//                if (data[0].contains(WANT_KILL.value)) {
//                    if (strength < Float.parseFloat(data[2])) {
//                        postOffice.notifyTo(data[1], GIVE_SOURCE.value + COMMA + sources);
//                        sources = 0;
//                        this.die();
//                    }
//
//                }
            }


        }
    }

    private double timeNow() {
        return systemNanoTime();
    }

    private String name() {
        return thread.getName();
    }

    void salary(double timeInterval) {
        sources = sources + income(timeInterval);
    }

    private double income(double timeInterval) {
        return (1 * talent) * timeInterval;
    }

    void liveCosts(double timeInterval) {
        sources = sources - (1 * timeInterval);

    }

    void die() {
        postOffice.removeMailbox(name());
        isAlive = false;
    }

    Thread thread() {
        return thread;
    }

    @Override
    public String toString() {
        return "Entita{" + name() +
                ", talent=" + talent +
                '}';
    }
}
