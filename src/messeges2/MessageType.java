package messeges2;

public enum MessageType {
    WANT_BABY("1"),
    ACCEPT_BABY("2"),
    GIVE_SOURCE("3"),
    WANT_STEAL("4"),
    WANT_KILL("5"),
    ;

    public final String value;

    MessageType(String value) {
        this.value = value;
    }
}
