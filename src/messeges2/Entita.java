package messeges2;


import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ThreadLocalRandom;

import static messeges2.MessageType.*;
import static messeges2.StringUtils.d;
import static messeges2.StringUtils.s;
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
    private Matrika matrika;
    private Queue<String[]> messages;
    private Queue<String[]> potentialPartners;
    private List<Double> intervalList;
    private boolean isAlive;
    private boolean isLookingForPartner;
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

    public Entita(Matrika matrika, PostOffice postOffice, double sources, double talent) {
        this.sources = sources;
        this.talent = talent;
        this.postOffice = postOffice;
        this.matrika = matrika;
        this.timestampPresent = timeNow(); //actual time zero
        this.timestampEnd = timestampPresent + (100000000 * ThreadLocalRandom.current().nextDouble(50.0, 85.0));
        this.isAlive = true;
        this.isLookingForPartner = true;
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
        matrika.createNewRecord(name());
        thread.start();
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
//        System.out.println("Hello I am new " + name() + ", " + talent + ", " + patience);
//        this.timestampPresent = timeNow(); //actual time zero
//        this.timestampEnd = timestampPresent + (100000000 * ThreadLocalRandom.current().nextDouble(50.0, 85.0));

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
            if (timestampPresent > timestampEnd || sources < 0) {
                die();
                postOffice.removeMailbox(name());
                matrika.removeRecord(name());
                System.out.println("SMRT!!! " + name());
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
                postOffice.notifyAll(name(), LOOKING_FOR_PARTNER.value, s(talent), s(sources), name());
//                System.out.println(name() + " notifying all - LOOKING_FOR_PARTNER");

                if (timeout == 0) {
                    // set time stamp, when entity will must decide
                    timeout = (9000 * patience);
//                    timeout = (calculateAverage(intervalList) * patience);
                }
                isLookingForPartner = false;
            }
            // 02 - Process the intent
            if (intentDecideWhoIsPartnerRightNow) {
                //pokud vyprsel cas (timeout) pro hledani partnera, dojdu k rozhodnuti a oznamim mu ze mam s nim dite
                // candidate of having baby
                // option 1 - accept first
                // option 2 - accept best by some criterium talent, age, strength, patience
                String mostTalented = "0.0";
                String[] best = null;

                for (String[] data : potentialPartners) {
                    if (data[0].compareTo(mostTalented) > 0) {
                        mostTalented = data[0];
                        best = data;
                    }
                }
                if (best != null) {
//                    System.out.println(name()+" has make a child for nTicks: "+nTicks);

                    sources = sources - CHILD_EXPENSE;
                    Entita child = new Entita(matrika, postOffice, 0, (talent + d(best[1])) / 2);
                    postOffice.notifyTo(best[2], YOU_ARE_DAD.value, s(talent), s(sources), name());
                    matrika.oznamPodatelne(name(), new String[]{WE_HAVE_A_BABY.value, child.name(), best[2]});
                    timeStopWatch = 0;
                    nTicks = 0;
                    isLookingForPartner = true;
                    potentialPartners.clear();
//                    System.out.println("We did IT!!!! mother: " + this.name() + ", father: " + best[2] + ", new born: " + child.name());
                }
            }
            // 03 - Process the intent
            if (intentSteal) {
                postOffice.notifyRandom(WANT_STEAL.value, name(), s(perception));
            }
            // 04 - Process the intent
            if (intentMurder) {
                postOffice.notifyRandom(WANT_KILL.value, name(), s(strength));
            }


            // Decide about your intent / Choose your intent
            intentLookForYourNewPartner = sources > CHILD_EXPENSE && isLookingForPartner;
            intentDecideWhoIsPartnerRightNow = timeStopWatch > timeout;
            intentSteal = timeStopWatch > ((calculateAverage(intervalList) * patience) * 10) && perception > strength;
            intentMurder = timeStopWatch < 0;


            /// Phase 2 - process messages
            while (!messages.isEmpty()) {
                String[] message = messages.poll();//read and del

                if (message[0].equals(LOOKING_FOR_PARTNER.value)) {
                    //odpovidam yes partner (nebo mlcim a ignoruji)
                    postOffice.notifyTo(message[3], CONFIRM_PARTNER.value, s(talent), s(sources), name());
//                    System.out.println(name() + " Processing - LOOKING_FOR_PARTNER");
                }
                if (message[0].equals(CONFIRM_PARTNER.value)) {
                    potentialPartners.add(new String[]{message[1], message[2], message[3]});
//                    System.out.println(" Processing - YES_PARTNER : Entity " + name() + " accepting " + message[3] + " as a partner...");
                }
                if (message[0].equals(YOU_ARE_DAD.value)) {
                    if (sources > CHILD_EXPENSE) {
                        sources -= CHILD_EXPENSE;
                        postOffice.notifyTo(message[3], GIVE_SOURCE.value, s(CHILD_EXPENSE));
                    } else {
                        System.out.println(" Im out of sources!!!!");
                    }
//                    System.out.println(name() + " Processing - YOU_ARE_DAD");
                }
                if (message[0].equals(GIVE_SOURCE.value)) {
                    double gift = d(message[1]);
                    sources = sources + gift;
//                    System.out.println(name() + " Processing - GIVE_SOURCE");
                }
                if (message[0].equals(WANT_STEAL.value)) {
                    if (perception < d(message[2])) {
                        postOffice.notifyTo(message[1], GIVE_SOURCE.value, s(sources));
                        sources = 0;
                    }
                }
                if (message[0].contains(WANT_KILL.value)) {
                    if (strength < d(message[2])) {
                        postOffice.notifyTo(message[1], GIVE_SOURCE.value, s(sources));
                        sources = 0;
                        this.die();
                    }

                }
            }


        }
        System.out.println("Its not fair! I has been murdered :/");
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
