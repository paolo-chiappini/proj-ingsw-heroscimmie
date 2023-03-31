package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.interfaces.IPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The class is responsible for handling the progression of turns
 * in a game. The class also checks whether
 */
public class TurnManager {
    private int currentPlayer;
    private boolean lastLap;
    private final List<IPlayer> players;

    /**
     * @param players list of players playing the game
     */
    public TurnManager(List<IPlayer> players) {
        this.players = new ArrayList<>(players);
        Collections.shuffle(this.players);
    }

    /**
     * Checks if the current player has met the end game condition.
     */
    private void checkEndCondition() {
        lastLap = players.get(currentPlayer).getBookshelf().isFull();
    }

    /**
     * Checks whether the game has ended or turns can still be played.
     * @return true if the game has ended, false otherwise.
     */
    public boolean isGameOver() {
        boolean firstPlayerTurn = currentPlayer == 0;
        return lastLap && firstPlayerTurn;
    }

    /**
     * Progresses the turn (if possible) moving to the next player.
     * @throws IllegalActionException when trying to move to the next turn when game has ended.
     */
    public void nextTurn() throws IllegalActionException {
        if (isGameOver()) {
            throw new IllegalActionException("The current game is over, there are no more turns to play. Please check with TurnManager.isGameOver().");
        }

        currentPlayer++;
        currentPlayer = currentPlayer % players.size();
        checkEndCondition();
    }

    /**
     * Get the current player playing the turn.
     * @return the player playing the turn.
     */
    public IPlayer getCurrentPlayer() {
        return players.get(currentPlayer);
    }
}
