package it.polimi.ingsw.model.interfaces.builders;

import it.polimi.ingsw.model.interfaces.IPlayer;
import it.polimi.ingsw.model.interfaces.ITurnManager;
import it.polimi.ingsw.exceptions.IllegalActionException;

/**
 * This interface represents a builder for objects of type
 * ITurnManager.
 */
public interface ITurnManagerBuilder extends Builder<ITurnManager> {
    /**
     * Add a player to the list of players. The players are added in order
     * of call:
     * i.e. addPlayer(p1); addPlayer(p3); addPlayer(p2); results in the following
     * players order: p1 -> p3 -> p2.
     * @param player player to add.
     * @return the builder instance.
     * @throws IllegalActionException when trying to add a new player while having
     * reached the maximum number of players in a game.
     * @throws IllegalArgumentException when trying to add a null player object.
     */
    ITurnManagerBuilder addPlayer(IPlayer player);

    /**
     * Set the current turn to the parameter value.
     * @param turn new value of turn.
     * @return the builder instance.
     * @throws IllegalArgumentException when the turn value is either negative or greater
     * than the number of players.
     */
    ITurnManagerBuilder setCurrentTurn(int turn);

    /**
     * Set the current lap to be the last.
     * @param endgame if true, the current lap will be the last.
     * @return the builder instance.
     */
    ITurnManagerBuilder setIsEndGame(boolean endgame);
}
