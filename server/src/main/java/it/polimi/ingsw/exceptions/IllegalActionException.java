package it.polimi.ingsw.exceptions;

/**
 * Signals that the invoked method cannot fulfill the requested action
 * given the current state.
 * <p>Could indicate that the method has been invoked unchecked, any call
 * to the method should check the state beforehand.
 */
public class IllegalActionException extends RuntimeException {
    public IllegalActionException(String message) {
        super(message);
    }
}
