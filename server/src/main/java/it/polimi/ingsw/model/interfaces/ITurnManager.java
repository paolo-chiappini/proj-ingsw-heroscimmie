package it.polimi.ingsw.model.interfaces;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.util.serialization.Serializable;

import java.util.List;

public interface ITurnManager extends Serializable {
    /**
     * Returns whether the game is in the endgame phase.
     * @return true if the game is in endgame, false otherwise.
     */
    boolean isLastLap();

    /**
     * Checks whether the game has ended or turns can still be played.
     * @return true if the game has ended, false otherwise.
     */
    boolean isGameOver();

    /**
     * Progresses the turn (if possible) moving to the next player.
     * @throws IllegalActionException when trying to move to the next turn when game has ended.
     */
    void nextTurn();

    /**
     * Get the current player playing the turn.
     * @return the player playing the turn.
     */
    IPlayer getCurrentPlayer();

    /**
     * Get the list of players in the order in which they
     * play their turn.
     * @return the list of players ordered by turn.
     */
    List<IPlayer> getPlayersInOrder();
}
