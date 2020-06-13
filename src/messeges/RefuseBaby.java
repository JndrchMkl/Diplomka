package messeges;

import entitytask.SystemCore;

public class RefuseBaby implements Message {

    private Entity sender;
    private Entity receiver;

    public RefuseBaby(Entity sender, Entity receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }

    @Override
    public void process() {
        sender.setSources(sender.getSources() + SystemCore.CHILD_EXPENSE);//unblock resources
        receiver.setSources(receiver.getSources() + SystemCore.CHILD_EXPENSE);//unblock resources
    }
}
