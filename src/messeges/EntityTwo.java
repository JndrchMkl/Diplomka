package messeges;

import entitytask.SystemCore;
import javafx.concurrent.Task;

import java.util.LinkedList;
import java.util.List;

public class EntityTwo extends Task<EntityTwo> {
    boolean alive = true;
    double sources = 0;
    private List<Message> messages = new LinkedList<>();
    private List<EntityTwo> children = new LinkedList<>();

    public void addMessage(Message m) {

        if (alive) {
            messages.add(m);
        }
    }

    public EntityTwo call() {
        while (alive) {
            if (messages.size() > 0) {
                Message m = messages.remove(0);
                m.process();
            } else {
                // do babies
                if (sources > SystemCore.CHILD_EXPENSE) {
                    //find best parner for you

                    // send request

                }
            }
        }
        return this;
    }

    public double getSources() {
        return sources;
    }

    public void setSources(double sources) {
        this.sources = sources;
    }

    public List<EntityTwo> getChildren() {
        return children;
    }

    public void setChildren(List<EntityTwo> children) {
        this.children = children;
    }
}
