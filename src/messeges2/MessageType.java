package messeges2;

public enum MessageType {
    WANT_BABY(1),
    ACCEPT_BABY(2),
    GIVE_SOURCE(3),
    WANT_STEAL(4),
    WANT_KILL(5),
    ;

    public final int id;

    MessageType(int id) {
        this.id = id;
    }
}
