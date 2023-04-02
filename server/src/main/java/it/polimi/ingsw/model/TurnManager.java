package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.interfaces.IPlayer;
import it.polimi.ingsw.model.interfaces.ITurnManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The class is responsible for handling the progression of turns
 * in a game. The class also checks whether
 */
public class TurnManager implements ITurnManager {
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

    @Override
    public boolean isGameOver() {
        boolean firstPlayerTurn = currentPlayer == 0;
        return lastLap && firstPlayerTurn;
    }

    @Override
    public void nextTurn() throws IllegalActionException {
        if (isGameOver()) {
            throw new IllegalActionException("The current game is over, there are no more turns to play. Please check with TurnManager.isGameOver().");
        }

        currentPlayer++;
        currentPlayer = currentPlayer % players.size();
        checkEndCondition();
    }

    @Override
    public IPlayer getCurrentPlayer() {
        return players.get(currentPlayer);
    }

    @Override
    public List<IPlayer> getPlayersOrder() {
        return new ArrayList<>(players);
    }
}
