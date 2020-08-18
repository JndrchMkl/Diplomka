package cz.upa.simulation.domain;


import cz.upa.simulation.graph.RecorderSizePerTime;
import cz.upa.simulation.messaging.PostOffice;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.SplittableRandom;
import java.util.concurrent.ThreadLocalRandom;

import static cz.upa.simulation.domain.Settings.ACTUAL_POPULATION_TALENT;
import static cz.upa.simulation.domain.Settings.IS_SIMULATION_RUNNING;
import static cz.upa.simulation.messaging.MessageType.*;
import static cz.upa.simulation.utils.StringUtils.d;
import static cz.upa.simulation.utils.StringUtils.s;
import static cz.upa.simulation.utils.TimeUtils.systemNanoTime;

public class Entita implements Runnable {

    // Character attributes
    private double patience;
    private double strength;
    private double perception;

    private double sources;
    private double talent;
    private double timestampBorn;
    private double timestampPresent;
    private double timestampEnd;
    private double timeout;
    private String parrentA;
    private String parrentB;
    private List<String> children;
    private PostOffice postOffice;
    private Matrika matrika;
    private Queue<String[]> messages;
    private Queue<String[]> potentialPartners;
    private List<Double> intervalList;
    private boolean hasRecord = false;
    private boolean isAlive;
    private boolean isLookingForPartner;
    private boolean intentLookForYourNewPartner;
    private boolean intentDecideWhoIsPartnerRightNow;
    private boolean intentSteal;
    private boolean intentMurder;
    private boolean intentPunishment;
    private boolean intentSecondaryPunishment;
    private boolean intentGrowSociety;
    private boolean hasPunished;
    private Thread thread;
    private int nTicks = 0;

    private double timeStopWatch = 0;

    public Entita(Matrika matrika, PostOffice postOffice, double sources, double talent, String parrentA, String parrentB) {
        this.sources = sources;
        this.talent = talent;
        this.postOffice = postOffice;
        this.matrika = matrika;
        this.parrentA = parrentA;
        this.parrentB = parrentB;
        this.isAlive = true;
        this.isLookingForPartner = true;
        this.intentLookForYourNewPartner = false;
        this.intentDecideWhoIsPartnerRightNow = false;
        this.intentSteal = false;
        this.intentMurder = false;
        this.intentPunishment = false;
        this.intentSecondaryPunishment = false;
        this.intentGrowSociety = false;
        this.hasPunished = false;
        patience = ThreadLocalRandom.current().nextDouble(Settings.RANGE_PATIENCE_FROM, Settings.RANGE_PATIENCE_TO);
        strength = ThreadLocalRandom.current().nextDouble(Settings.RANGE_STRENGTH_FROM, Settings.RANGE_STRENGTH_TO);
        perception = ThreadLocalRandom.current().nextDouble(Settings.RANGE_PERCEPTION_FROM, Settings.RANGE_PERCEPTION_TO);
        children = new LinkedList<>();
        messages = new LinkedList<>();
        potentialPartners = new LinkedList<>();
        intervalList = new LinkedList<>();
        thread = new Thread(this);
        postOffice.createMailbox(name());
        matrika.createNewRecord(name());
        this.timestampBorn = timeNow();
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

        this.timestampPresent = timeNow(); //actual time zero
        this.timestampEnd = timestampBorn + (Settings.MULTIPLIER_LIVE_LENGTH * ThreadLocalRandom.current().nextDouble(Settings.RANGE_LIFE_LENGTH_FROM, Settings.RANGE_LIFE_LENGTH_TO));

        while (isAlive) {
            /// Phase 1 - upkeep
            // timestamps
            double now = timeNow();
            double interval = now - timestampPresent; // nutno pocitat s intervalem rovnym 0. POZOR!!! Nedělit s intervalem rovným 0!!!
            timestampPresent = now;
            timeStopWatch += interval;
            intervalList.add((interval));
            nTicks++;


            // zemri starim (nebo hladověním)
            if (timestampPresent > timestampEnd || sources < 0 || !IS_SIMULATION_RUNNING) {
                ACTUAL_POPULATION_TALENT -= talent;
                die();
                postOffice.removeMailbox(name());
                RecorderSizePerTime.recordMessages.add(new Double[]{0.0, timestampPresent});
//                System.out.println("SMRT!!! " + name());
                return;
            }

            // income
            messages.addAll(postOffice.withdrawMessages(name()));
            salary(interval);
            liveCosts(interval);


            // Intents
            // 01 - Process the intent
            if (intentLookForYourNewPartner) { // hledam partnera
                postOffice.notifyAll(name(), LOOKING_FOR_PARTNER.value, s(talent), s(sources), name());

                if (timeout == 0) {
                    // set time stamp, when entity will must decide
                    timeout = (Settings.MULTIPLIER_TIMEOUT_INTENT_LOOK_FOR_PARTNER * patience);
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
                    sources = sources - Settings.VALUE_CHILD_EXPENSE;
                    double talent = (this.talent + d(best[1])) / 2;
                    ACTUAL_POPULATION_TALENT += talent;
                    Entita child = new Entita(matrika, postOffice, 0, talent,name(),best[2]);
                    postOffice.notifyTo(best[2], YOU_ARE_DAD.value, s(this.talent), s(sources), name(), child.name());
                    RecorderSizePerTime.recordMessages.add(new Double[]{1.0, timestampPresent});

                    children.add(child.name());
                    timeStopWatch = 0;
                    isLookingForPartner = true;
                    potentialPartners.clear();
                }
            }
            // 03 - Process the intent
            if (intentSteal) {
                postOffice.notifyRandom(WANT_STEAL.value, name(), s(perception), s(hasPunished));
            }
            // 04 - Process the intent
            if (intentMurder) {
                postOffice.notifyRandom(WANT_KILL.value, name(), s(strength), s(hasPunished));
            }


            // Decide about your intent / Choose your intent
            intentLookForYourNewPartner = sources > Settings.VALUE_CHILD_EXPENSE && isLookingForPartner && Settings.INTENT_LOOK_FOR_PARTNER;
            intentDecideWhoIsPartnerRightNow = timeStopWatch > timeout && Settings.INTENT_DECIDE_RIGHT_NOW;
            intentSteal = timeStopWatch > ((calculateAverage(intervalList) * patience) * 10) && Settings.INTENT_STEAL;
            intentMurder = timeStopWatch > ((calculateAverage(intervalList) * patience) * 25) && Settings.INTENT_MURDER;
            intentPunishment = Settings.INTENT_PUNISHMENT;
            intentSecondaryPunishment = Settings.INTENT_SECONDARY_PUNISHMENT;

            /// Phase 2 - process messages
            while (!messages.isEmpty()) {
                String[] message = messages.poll();//read and del

                if (message[0].equals(LOOKING_FOR_PARTNER.value)) {
                    //odpovidam yes partner (nebo mlcim a ignoruji)
                    postOffice.notifyTo(message[3], CONFIRM_PARTNER.value, s(talent), s(sources), name());
                }
                if (message[0].equals(CONFIRM_PARTNER.value)) {
                    potentialPartners.add(new String[]{message[1], message[2], message[3]});
                }
                if (message[0].equals(YOU_ARE_DAD.value)) {
                    children.add(message[4]);
                    if (sources > Settings.VALUE_CHILD_EXPENSE) {
                        sources -= Settings.VALUE_CHILD_EXPENSE;
                        postOffice.notifyTo(message[3], GIVE_SOURCE.value, s(Settings.VALUE_CHILD_EXPENSE));
                    } else {
                        System.out.println(" Im out of sources!!!!");
                    }
                }
                if (message[0].equals(GIVE_SOURCE.value)) {
                    double gift = d(message[1]);
                    sources = sources + gift;
                }
                if (message[0].equals(WANT_STEAL.value)) {
                    if (perception < d(message[2])) {
                        postOffice.notifyTo(message[1], GIVE_SOURCE.value, s(sources));
                        sources = 0;
                    } else if (intentPunishment) {
                        if (s(false).equals(message[3])) {
                            postOffice.notifyTo(message[1], WANT_PUNISHMENT.value, name());
                        } else if (s(true).equals(message[3]) && intentSecondaryPunishment) {
                            postOffice.notifyTo(message[1], WANT_SECONDARY_PUNISHMENT.value, name());
                        }
                    }
                }
                if (message[0].contains(WANT_KILL.value)) {
                    if (strength < d(message[2])) {
                        postOffice.notifyTo(message[1], GIVE_SOURCE.value, s(sources));
                        sources = 0;
                        die();
                    }
                }
                if (message[0].equals(WANT_PUNISHMENT.value)) {
                    hasPunished = true;
                    int chance = new SplittableRandom().nextInt(0, 100);
                    if (chance < 50) {
                        // Send him your money // vs throw away money // vs donate society
                        sources = 0;
                    } else if (chance < 90) {
                        // execute you
                        die();
                    } else {
                        postOffice.notifyTo(parrentA,EXECUTE_BLOOD_RELATED.value,name());
                        postOffice.notifyTo(parrentB,EXECUTE_BLOOD_RELATED.value,name());
                        for (String c:children) {
                            postOffice.notifyTo(c,EXECUTE_BLOOD_RELATED.value,name());
                        }
                        die();
                    }
                }
                if (message[0].equals(WANT_SECONDARY_PUNISHMENT.value)) {
                    // KILL YOUR MOTHER AND FATHER, YOUR CHILDREN and yourself
                    postOffice.notifyTo(parrentA,EXECUTE_BLOOD_RELATED.value,name());
                    postOffice.notifyTo(parrentB,EXECUTE_BLOOD_RELATED.value,name());
                    for (String c:children) {
                        postOffice.notifyTo(c,EXECUTE_BLOOD_RELATED.value,name());
                    }
                    die();
                }
                if (message[0].equals(EXECUTE_BLOOD_RELATED.value)) {
                    die();
                }
            }
        }
//        RecorderSizePerTime.recordMessages.add(new Double[]{0.0, timestampPresent});
        System.out.println("Its not fair! I has been ended :/ " + name());
    }

    private double timeNow() {
        return systemNanoTime();
    }

    private String name() {
        return thread.getName();
    }

    private void salary(double timeInterval) {
        sources = sources + income(timeInterval);
    }

    private double income(double timeInterval) {
        return ((Settings.MULTIPLIER_INCOME * talent) / ACTUAL_POPULATION_TALENT) * Settings.C_SOURCES_POOL * timeInterval;
    }

    private void liveCosts(double timeInterval) {
        sources = sources - (Settings.MULTIPLIER_LIVE_COST * timeInterval);
    }

    public double getTimestampBorn() {
        return timestampBorn;
    }

    private void die() {
//        postOffice.removeMailbox(name());
//        isAlive = false;
        timestampPresent = timestampEnd + 1;
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
