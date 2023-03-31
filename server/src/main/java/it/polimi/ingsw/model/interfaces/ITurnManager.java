package it.polimi.ingsw.model.interfaces;

import it.polimi.ingsw.exceptions.IllegalActionException;

public interface ITurnManager {
    /**
     * Checks whether the game has ended or turns can still be played.
     * @return true if the game has ended, false otherwise.
     */
    boolean isGameOver();

    /**
     * Progresses the turn (if possible) moving to the next player.
     * @throws IllegalActionException when trying to move to the next turn when game has ended.
     */
    void nextTurn() throws IllegalActionException;

    /**
     * Get the current player playing the turn.
     * @return the player playing the turn.
     */
    IPlayer getCurrentPlayer();
}
