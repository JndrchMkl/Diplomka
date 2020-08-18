package cz.upa.simulation.domain;

public abstract class Settings {
    public static boolean INTENT_PUNISHMENT = true;
    public static boolean INTENT_SECONDARY_PUNISHMENT = true;
    public static boolean INTENT_GROW_SOCIETY = true;
    public static boolean INTENT_LOOK_FOR_PARTNER = true;
    public static boolean INTENT_DECIDE_RIGHT_NOW = true;
    public static boolean INTENT_STEAL = true;
    public static boolean INTENT_MURDER = true;
    public static int MULTIPLIER_LIVE_COST = 5;
    public static int MULTIPLIER_INCOME = 1;
    public static int MULTIPLIER_TIMEOUT_INTENT_LOOK_FOR_PARTNER = 9000;
    public static int MULTIPLIER_LIVE_LENGTH = 100000000;
    public static double RANGE_LIFE_LENGTH_FROM = 50.0;
    public static double RANGE_LIFE_LENGTH_TO = 385.0;
    public static double RANGE_PATIENCE_FROM = 1.0;
    public static double RANGE_PATIENCE_TO = 10.0;
    public static double RANGE_STRENGTH_FROM = 1.0;
    public static double RANGE_STRENGTH_TO = 10.0;
    public static double RANGE_PERCEPTION_FROM = 1.0;
    public static double RANGE_PERCEPTION_TO = 10.0;
    public static double RANGE_TALENT_FROM = 5.0;
    public static double RANGE_TALENT_TO = 10.0;
    public static int VALUE_CHILD_EXPENSE = 1;
    public static int SIZE_ENTITY_SET = 10;
    public static boolean IS_SIMULATION_RUNNING = true;
    public static int ACTUAL_ID_BUILD = 1;
    public static int SIMULATION_DELAY_LENGTH = 60000;

    public static double ACTUAL_POPULATION_TALENT = 0; // SUM(Ta)
    public static double C_SOURCES_POOL = 4000; // C - priblizny pocet vlaken

}
