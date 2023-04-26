package it.polimi.ingsw.server;

import it.polimi.ingsw.server.messages.Message;

/**
 * Represents a middleware function that takes a request Message, a
 * response Message, a Callback function and returns nothing.
 */
public interface Middleware {
    void apply(Message request, Message response, Callback next);
}
