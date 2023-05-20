package it.polimi.ingsw.util.observer;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Represents a generic observable object.
 * @param <T> Type of listener.
 */
public abstract class ObservableObject<T extends Listener> implements Observable<T> {
    private final List<T> listeners = new LinkedList<>();

    /**
     * Adds a new listener to the object.
     * @param listener listener to add.
     */
    public void addListener(T listener) { listeners.add(listener); }

    /**
     * Removes a listener from the object (if present).
     * @param listener listener to remove.
     */
    public void removeListener(T listener) { listeners.remove(listener); }

    /**
     * Notifies all listeners currently listening to the object.
     * @param action action performed by the listeners.
     */
    public void notifyListeners(Consumer<T> action) {
        for (T listener : listeners) action.accept(listener);
    }
}
