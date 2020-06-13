package messeges;

import entitytask.SystemCore;

public class RequestBaby implements Message {
    private Entity sender;//partenA
    private Entity receiver;//partnerB

    public RequestBaby(Entity sender) {
        this.sender = sender;
        this.receiver = null;
    }

    public RequestBaby(Entity entity, Entity partner) {
        this.sender = entity;
        this.receiver = partner;
    }

    @Override
    public void process() {
        //request bz se mel posilat na druhou entitu
        //pokud souhlasi posle accept baby zpet na sendra
        if (sender.hasSources() && receiver.hasSources()) {
            sender.addMessage(new AcceptBaby(sender, receiver));
        } else {
            sender.addMessage(new RefuseBaby(sender, receiver));
        }
    }


    // zpracovani probiha ve vlakne receivera, takze na sendera nesahat
    // pokud jsou potreba informace o senderovi pridat pro ne aribut
//    public void process() {
//        // TODO find fittest
//        // TODO set fittest to receiver
//        // TODO send request baby message to receiver
//        if (receiver.getSources() > SystemCore.CHILD_EXPENSE) {
////            sender.setSources(sender.getSources() - SystemCore.CHILD_EXPENSE);//Block resources
//            sender.addMessage(new AcceptBaby(sender, receiver));
//        } else {
//            sender.addMessage(new RefuseBaby(sender, receiver));
//        }
//    }
}
