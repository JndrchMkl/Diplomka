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
        Entity child = crossover();

        //rekni entitam že mají dítě
        sender.getChildren().add(child);
        receiver.getChildren().add(child);

        // rekni populaci ze jim pribyl jedna nová entita
        sender.getPopulation().addEntity(child);

    }

    public Entity crossover() {

        //Select crossover value
        Double crossOverValueOne = sender.getTalent();
        Double crossOverValueTwo = receiver.getTalent();
        Entity child = new Entity(
                this.sender.getPopulation(),
                "E" + sender.getPopulation().state().size(),
                (crossOverValueOne + crossOverValueTwo) / 2,
                sender,
                receiver
        );
        return child;
    }

}
