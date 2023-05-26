package it.polimi.ingsw.server.model.game;

import it.polimi.ingsw.server.model.bag.IBag;
import it.polimi.ingsw.server.model.board.IBoard;
import it.polimi.ingsw.server.model.bookshelf.IBookshelf;
import it.polimi.ingsw.server.model.goals.AdjacencyBonusGoal;
import it.polimi.ingsw.server.model.goals.common.CommonGoalCard;
import it.polimi.ingsw.server.model.goals.personal.PersonalGoalCard;
import it.polimi.ingsw.server.model.player.IPlayer;
import it.polimi.ingsw.server.model.turn.ITurnManager;
import it.polimi.ingsw.util.serialization.Serializable;
import it.polimi.ingsw.util.serialization.Serializer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents the main game object responsible for managing the various
 * components of the game.
 */
public class Game implements Serializable {
    private final ITurnManager turnManager;
    private final IBag bag;
    private final IBoard board;
    private final List<CommonGoalCard> commonGoals;
    private IPlayer winner;

    /**
     * Creates a new instance of Game using a builder.
     * @param builder builder used to create the instance.
     */
    private Game(GameBuilder builder) {
        this.turnManager = builder.turnManager;
        this.bag = builder.bag;
        this.board = builder.board;
        this.commonGoals = new ArrayList<>(builder.commonGoals);
    }

    public void evaluateFinalScores() {
        AdjacencyBonusGoal adjacencyBonusGoal = new AdjacencyBonusGoal();
        List<IPlayer> players = turnManager.getPlayersInOrder();
        for (IPlayer player : players) {
            PersonalGoalCard personalCard = player.getPersonalGoalCard();
            IBookshelf bookshelf = player.getBookshelf();
            player.addPointsToScore(personalCard.evaluatePoints(bookshelf));
            player.addPointsToScore(adjacencyBonusGoal.evaluatePoints(bookshelf));
        }
    }

    /**
     * Selects a single players as the winner based on:
     * <ul>
     *     <li>the score: the player with the highest score wins;</li>
     *     <li>the position relative to the first player: in case of draw (in terms of scores), the furthest player from
     *     the first player wins.</li>
     * </ul>
     * @return the player that has been declared winner. Returns null if there's no player that can be declared winner.
     */
    public IPlayer getWinner() {
        // if winner has already been declared, return it
        if (winner != null) return winner;

        List<IPlayer> players = turnManager.getPlayersInOrder();
        LinkedList<IPlayer> highestScoringPlayers = new LinkedList<>();
        int maxScore = players.get(0).getScore();

        // get the highest score
        for (IPlayer player : players) maxScore = Integer.max(maxScore, player.getScore());
        // get players that scored the most points
        for (IPlayer player : players)
            if (player.getScore() == maxScore)
                highestScoringPlayers.add(player);

        // if there are multiple players, get the furthest from the first
        winner = highestScoringPlayers.getLast();

        return winner;
    }

    /**
     * Get the common goal cards set at the beginning of the game.
     * @return the list of common goals.
     */
    public List<CommonGoalCard> getCommonGoals() {
        return new ArrayList<>(commonGoals);
    }

    /**
     * Get the list of players in the current game.
     * @return the list of players in the game.
     */
    public List<IPlayer> getPlayers() {
        return new ArrayList<>(turnManager.getPlayersInOrder());
    }

    /**
     * Get the turn manager currently handling the progression of the game.
     * @return the turn manager.
     */
    public ITurnManager getTurnManager() {
        return turnManager;
    }

    /**
     * Get the tiles bag currently being used in the game.
     * @return the bag.
     */
    public IBag getBag() { return bag; }

    /**
     * Get the board currently being used in the game.
     * @return the board.
     */
    public IBoard getBoard() { return board; }

    /**
     * Serializes the (current) state of the game to a string of data based on the type of serializer chosen.
     * @param serializer type of serializer used to serialize data.
     * @return the serialized string of data representing the current game state.
     */
    @Override
    public String serialize(Serializer serializer) {
        return serializer.serialize(this);
    }

    /**
     * Builder used during the deserialization of the game state.
     */
    public static class GameBuilder implements IGameBuilder {
        private ITurnManager turnManager;
        private IBag bag;
        private IBoard board;
        private List<CommonGoalCard> commonGoals;

        @Override
        public Game build() {
            return new Game(this);
        }

        @Override
        public IGameBuilder setTurnManager(ITurnManager turnManager) {
            if (turnManager == null) {
                throw new IllegalArgumentException("Turn manager cannot be null.");
            }
            this.turnManager = turnManager;
            return this;
        }

        @Override
        public IGameBuilder setCommonGoalCards(List<CommonGoalCard> commonGoals) {
            if (commonGoals == null || commonGoals.size() != 2) {
                throw new IllegalArgumentException("Common goals should be 2");
            }
            this.commonGoals = new ArrayList<>(commonGoals);
            return this;
        }

        @Override
        public IGameBuilder setTilesBag(IBag bag) {
            if (bag == null) {
                throw new IllegalArgumentException("Bag cannot be null");
            }
            this.bag = bag;
            return this;
        }

        @Override
        public IGameBuilder setBoard(IBoard board) {
            if (board == null) {
                throw new IllegalArgumentException("Board cannot be null");
            }
            this.board = board;
            return this;
        }
    }
}
