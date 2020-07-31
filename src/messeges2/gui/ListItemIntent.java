package messeges2.gui;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleBooleanProperty;

public class ListItemIntent {
    private final ReadOnlyStringWrapper name = new ReadOnlyStringWrapper();
    private final BooleanProperty selected = new SimpleBooleanProperty(true);
    private boolean disabled;

    public ListItemIntent(String name) {
        this.name.set(name);
        disabled = false;
    }
    public ListItemIntent(String name,boolean disabled) {
        this.name.set(name);
        this.disabled = disabled;
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
        if (!disabled) {
            this.selected.set(selected);
        }
    }

    public boolean isDisabled() {
        return disabled;
    }

    @Override
    public String toString() {
        return "Intent{" +
                "name=" + name.get() +
                ", selected=" + selected.get() +
                '}';
    }
}
