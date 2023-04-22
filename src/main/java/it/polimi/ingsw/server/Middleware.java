package it.polimi.ingsw.server;

public interface Middleware<T> {
    void apply(T request, T response, Callback<T> next);
}
