package it.polimi.ingsw.util.observer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Implements the observer design pattern.
 * Allows client to notify other objects of its change
 */
public class ClientObservable {
    private List<ControllerObserver> observers = new ArrayList<>();

    public void addObserver(ControllerObserver observer)
    {
        this.observers.add(observer);
    }

    public void removeObserver(ControllerObserver observer)
    {
        this.observers.remove(observer);
    }

    public void notifyObservers(Consumer<ControllerObserver> function) {
        for(ControllerObserver observer : observers)
            function.accept(observer);
    }
}