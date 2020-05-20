package messeges;

import javafx.concurrent.Task;

import java.util.LinkedList;
import java.util.List;

public class Entity extends Task<Entity> {
    boolean alive = true;
    double sources = 0;
    private List<Message> messages = new LinkedList<>();
    private List<Entity> children = new LinkedList<>();

    public void addMessage(Message m) {

        if (alive) {
            messages.add(m);
        }
    }

    public Entity call() {
        while (alive) {
            if (messages.size() > 0) {
                Message m = messages.remove(0);
                m.process();
            } else {
                // do babies
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

    public List<Entity> getChildren() {
        return children;
    }

    public void setChildren(List<Entity> children) {
        this.children = children;
    }
}
