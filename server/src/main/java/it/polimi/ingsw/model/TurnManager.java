package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.interfaces.IPlayer;
import it.polimi.ingsw.model.interfaces.ITurnManager;
import it.polimi.ingsw.model.interfaces.builders.ITurnManagerBuilder;
import it.polimi.ingsw.util.serialization.Serializer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The class is responsible for handling the progression of turns
 * in a game. The class also checks whether the game is over or not.
 */
public class TurnManager implements ITurnManager {
    private static final int MAX_PLAYERS_COUNT = 4;
    private static final int FIRST_TO_END_BONUS_POINTS = 1;
    private int currentPlayerIndex;
    private boolean lastLap;
    private final List<IPlayer> players;

    /**
     * @param players list of players playing the game
     */
    public TurnManager(List<IPlayer> players) {
        this.players = new ArrayList<>(players);
        Collections.shuffle(this.players);
    }

    private TurnManager(TurnManagerBuilder builder) {
        players = new ArrayList<>(builder.players);
        lastLap = builder.lastLap;
        currentPlayerIndex = builder.currentTurn;
    }

    /**
     * Checks if the current player has met the end game condition.
     */
    private void checkEndCondition() {
        if (lastLap) return;
        lastLap = players.get(currentPlayerIndex).getBookshelf().isFull();
        // add bonus points to the first player with a full bookshelf
        if (lastLap) players.get(currentPlayerIndex).addPointsToScore(FIRST_TO_END_BONUS_POINTS);
    }

    @Override
    public boolean isLastLap() {
        return lastLap;
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
    public List<IPlayer> getPlayersInOrder() {
        return new ArrayList<>(players);
    }

    @Override
    public String serialize(Serializer serializer) {
        return serializer.serialize(this);
    }

    public static class TurnManagerBuilder implements ITurnManagerBuilder {
        private int currentTurn;
        private boolean lastLap;
        private final List<IPlayer> players;

        public TurnManagerBuilder() {
            players = new ArrayList<>();
        }

        @Override
        public ITurnManager build() {
            return new TurnManager(this);
        }

        @Override
        public ITurnManagerBuilder addPlayer(IPlayer player) {
            if (player == null) {
                throw new IllegalArgumentException("Player cannot be null");
            }
            if (players.size() == MAX_PLAYERS_COUNT) {
                throw new IllegalActionException("Maximum number of players exceeded");
            }
            players.add(player);
            return this;
        }

        @Override
        public ITurnManagerBuilder setCurrentTurn(int turn) {
            if (turn < 0 || turn >= this.players.size()) {
                throw new IllegalArgumentException("Turn must be positive and less (or equal) to the number of players playing.");
            }
            currentTurn = turn;
            return this;
        }

        @Override
        public ITurnManagerBuilder setIsEndGame(boolean endgame) {
            lastLap = endgame;
            return this;
        }
    }
}
