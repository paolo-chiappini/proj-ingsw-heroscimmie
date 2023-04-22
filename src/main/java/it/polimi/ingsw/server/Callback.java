package it.polimi.ingsw.server;

import it.polimi.ingsw.server.messages.Message;

/**
 * Represents a callback function that takes a request Message, a
 * response Message and returns nothing.
 */
public interface Callback {
    void apply(Message request, Message response);
}
