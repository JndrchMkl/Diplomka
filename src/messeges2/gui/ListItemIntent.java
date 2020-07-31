package messeges2.gui;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleBooleanProperty;

public class ListItemIntent {
    private final ReadOnlyStringWrapper name = new ReadOnlyStringWrapper();
    private final BooleanProperty selected = new SimpleBooleanProperty(true);

    public ListItemIntent(String name) {
        this.name.set(name);
    }

    public String getName() {
        return name.get();
    }

    public ReadOnlyStringProperty nameProperty() {
        return name.getReadOnlyProperty();
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }

    public boolean isSelected() {
        return selected.get();
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    @Override
    public String toString() {
        return "Intent{" +
                "name=" + name.get() +
                ", selected=" + selected.get() +
                '}';
    }
}
