package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.interfaces.IPlayer;
import it.polimi.ingsw.model.interfaces.ITurnManager;
import it.polimi.ingsw.util.Deserializer;
import it.polimi.ingsw.util.Serializer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The class is responsible for handling the progression of turns
 * in a game. The class also checks whether
 */
public class TurnManager implements ITurnManager {
    private int currentPlayerIndex;
    private boolean lastLap;
    private List<IPlayer> players;

    /**
     * @param players list of players playing the game
     */
    public TurnManager(List<IPlayer> players) {
        this.players = new ArrayList<>(players);
        Collections.shuffle(this.players);
    }

    public TurnManager() {
        this.players = new ArrayList<>();
    }

    /**
     * Checks if the current player has met the end game condition.
     */
    private void checkEndCondition() {
        lastLap = players.get(currentPlayerIndex).getBookshelf().isFull();
    }

    @Override
    public boolean isGameOver() {
        boolean firstPlayerTurn = currentPlayerIndex == 0;
        return lastLap && firstPlayerTurn;
    }

    @Override
    public void nextTurn() {
        if (isGameOver()) {
            throw new IllegalActionException("The current game is over, there are no more turns to play. Please check with TurnManager.isGameOver().");
        }

        currentPlayerIndex++;
        currentPlayerIndex = currentPlayerIndex % players.size();
        checkEndCondition();
    }

    @Override
    public IPlayer getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    @Override
    public List<IPlayer> getPlayersOrder() {
        return new ArrayList<>(players);
    }

    @Override
    public void setPlayersOrder(List<IPlayer> players) {
        this.players = new ArrayList<>(players);
    }

    @Override
    public void setCurrentTurn(int turn) {
        if (turn < 0 || turn > this.players.size()) {
            throw new IllegalArgumentException("Turn must be positive and less (or equal) to the number of players playing.");
        }
        currentPlayerIndex = turn;
    }

    @Override
    public String serialize(Serializer serializer) {
        return serializer.serializeTurn(this);
    }

    @Override
    public void deserialize(Deserializer deserializer, String data) {
        deserializer.deserializeTurn(this, data);
    }
}
