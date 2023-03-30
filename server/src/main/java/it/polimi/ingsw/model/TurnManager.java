package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.interfaces.IPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TurnManager {
    private int currentPlayer;
    private boolean lastLap;
    private final List<IPlayer> players;

    public TurnManager(List<IPlayer> players) {
        this.players = new ArrayList<>();
        // Copy players
        this.players.addAll(players);
        Collections.shuffle(this.players);
    }

    private void checkEndCondition() {
        lastLap = players.get(currentPlayer).getBookshelf().isFull();
    }

    public boolean isGameOver() {
        boolean firstPlayerTurn = currentPlayer == 0;
        return lastLap && firstPlayerTurn;
    }

    public void nextTurn() throws IllegalActionException {
        if (isGameOver()) {
            throw new IllegalActionException("The current game is over, there are no more turns to play. Please check with TurnManager.isGameOver().");
        }

        currentPlayer++;
        currentPlayer = currentPlayer % players.size();
        checkEndCondition();
    }

    public IPlayer getCurrentPlayer() {
        return players.get(currentPlayer);
    }
}
