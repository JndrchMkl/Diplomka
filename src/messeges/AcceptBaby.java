package messeges;

import entitytask.SystemCore;

public class AcceptBaby implements Message {

    private Entity sender;
    private Entity receiver;

    public AcceptBaby(Entity sender, Entity receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }


    // zpracovani probiha ve vlakne receivera, takze na sendera nesahat
    // pokud jsou potreba informace o senderovi pridat pro ne aribut
    public void process() {
        Entity child = new Entity();
        sender.setSources(sender.getSources() - SystemCore.CHILD_EXPENSE);
        sender.getChildren().add(child);
        receiver.setSources(receiver.getSources() - SystemCore.CHILD_EXPENSE);
        receiver.getChildren().add(child);
    }

}
