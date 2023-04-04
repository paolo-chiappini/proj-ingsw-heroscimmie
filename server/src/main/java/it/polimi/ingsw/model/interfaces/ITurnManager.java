package it.polimi.ingsw.model.interfaces;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.util.Serializable;

import java.util.List;

public interface ITurnManager extends Serializable {
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
    List<IPlayer> getPlayersOrder();

    /**
     * Set the order in which the players should play.
     * @param players list of players in the order in which they play.
     */
    void setPlayersOrder(List<IPlayer> players);

    /**
     * Set the current turn to the parameter value.
     * @param turn new value of turn.
     * @throws IllegalArgumentException when the turn value is either negative or greater
     * than the number of players.
     */
    void setCurrentTurn(int turn);
}
