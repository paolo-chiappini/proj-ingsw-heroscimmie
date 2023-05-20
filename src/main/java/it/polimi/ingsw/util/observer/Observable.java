package it.polimi.ingsw.util.observer;

import java.util.function.Consumer;

public interface Observable<T extends Listener>{
    void addListener(T listener);
    void removeListener(T listener);
    void notifyListeners(Consumer<T> action);
}
