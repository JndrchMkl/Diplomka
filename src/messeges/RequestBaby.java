package messeges;

import entitytask.SystemCore;

public class RequestBaby {
    private Entity sender;
    private Entity receiver;

    public RequestBaby(Entity sender) {
        this.sender = sender;
        this.receiver = null;
    }

    // zpracovani probiha ve vlakne receivera, takze na sendera nesahat
    // pokud jsou potreba informace o senderovi pridat pro ne aribut
    public void process() {
        // TODO find fittest
        // TODO set fittest to receiver
        // TODO send request baby message to receiver
        if (receiver.getSources() > SystemCore.CHILD_EXPENSE) {
//            sender.setSources(sender.getSources() - SystemCore.CHILD_EXPENSE);//Block resources
            sender.addMessage(new AcceptBaby(sender, receiver));
        } else {
            sender.addMessage(new RefuseBaby(sender, receiver));
        }
    }
}
