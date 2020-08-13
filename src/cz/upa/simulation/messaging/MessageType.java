package cz.upa.simulation.messaging;

public enum MessageType {
    LOOKING_FOR_PARTNER("1"),
    CONFIRM_PARTNER("2"),
    YOU_ARE_DAD("3"),
    GIVE_SOURCE("4"),

    WANT_STEAL("5"),
    WANT_KILL("6"),

    WE_HAVE_A_BABY("7"),
    I_DIED("8");


    public final String value;

    MessageType(String value) {
        this.value = value;
    }
}
