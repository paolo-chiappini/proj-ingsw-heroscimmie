package it.polimi.ingsw.util.observer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Implements the observer design pattern.
 * Allows model to notify other objects of its change
 */
public class ModelObservable {
    private final List<ViewObserver> observers = new ArrayList<>();

    public void addObserver(ViewObserver observer)
    {
        this.observers.add(observer);
    }

    public void removeObserver(ViewObserver observer)
    {
        this.observers.remove(observer);
    }

    public void notifyObservers(Consumer<ViewObserver> function) {
        for(ViewObserver observer : observers)
                function.accept(observer);
    }
}
