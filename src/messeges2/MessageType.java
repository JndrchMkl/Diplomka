package messeges2;

public enum MessageType {
    LOOKING_FOR_PARTNER("1"),
    YOU_ARE_DAD("2"),
    GIVE_SOURCE("3"),
    WANT_STEAL("4"),
    WANT_KILL("5"),
    YES_PARTNER("6"),
    ;

    public final String value;

    MessageType(String value) {
        this.value = value;
    }
}
