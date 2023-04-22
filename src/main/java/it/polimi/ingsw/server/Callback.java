package it.polimi.ingsw.server;

public interface Callback<T> {
    void apply(T request, T response);
}
