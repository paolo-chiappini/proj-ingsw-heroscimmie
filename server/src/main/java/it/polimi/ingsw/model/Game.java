package it.polimi.ingsw.model;

import it.polimi.ingsw.model.interfaces.*;
import it.polimi.ingsw.model.interfaces.builders.IGameBuilder;
import it.polimi.ingsw.util.serialization.Serializable;
import it.polimi.ingsw.util.serialization.Serializer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Game implements Serializable {
    private final ITurnManager turnManager;
    private final IBag bag;
    private final IBoard board;
    private List<CommonGoalCard> commonGoals;
    private IPlayer winner;

    public Game(List<IPlayer> players, ITurnManager turnManager, IBag bag, IBoard board) {
        if (players == null || players.size() < 2) {
            throw new IllegalArgumentException("There should be at least 2 players");
        }
        this.turnManager = turnManager;
        this.bag = bag;
        this.board = board;

        assignCommonGoals();
        assignPersonalGoals();
    }

    private Game(GameBuilder builder) {
        this.turnManager = builder.turnManager;
        this.bag = builder.bag;
        this.board = builder.board;
        this.commonGoals = new ArrayList<>(builder.commonGoals);
    }

    private void assignPersonalGoals() {
        PersonalGoalCardDeck personalGoalsDeck = new PersonalGoalCardDeck();
        getPlayers().forEach(p -> {
            p.setPersonalGoalCard(personalGoalsDeck.drawCard());
        });
    }

    private void assignCommonGoals() {
        commonGoals = new CommonGoalCardDeck(getPlayers().size()).drawCards();
    }

    public void evaluateFinalScores() {
        List<IPlayer> players = turnManager.getPlayersOrder();
        for (IPlayer player : players) {
            PersonalGoalCard personalCard = player.getPersonalGoalCard();
            IBookshelf bookshelf = player.getBookshelf();
            player.addPointsToScore(personalCard.evaluatePoints(bookshelf));
        }
    }

    public IPlayer getWinner() {
        // if winner has already been declared, return it
        if (winner != null) return winner;

        List<IPlayer> players = turnManager.getPlayersOrder();

        IPlayer topPlayer = players.stream()
                .max(Comparator.comparingInt(IPlayer::getScore))
                .orElse(null);

        List<IPlayer> playersWithHighestScores = players.stream()
                .filter(p -> p.getScore() == topPlayer.getScore())
                .toList();

        winner = playersWithHighestScores.stream()
                .sorted(Comparator.comparingInt(players::indexOf))
                .reduce((first, second) -> second)
                .orElse(null);

        return winner;
    }

    public List<CommonGoalCard> getCommonGoals() {
        return new ArrayList<>(commonGoals);
    }

    public List<IPlayer> getPlayers() {
        return new ArrayList<>(turnManager.getPlayersOrder());
    }

    public ITurnManager getTurnManager() {
        return turnManager;
    }

    public IBag getBag() { return bag; }

    public IBoard getBoard() { return board; }

    @Override
    public String serialize(Serializer serializer) {
        return serializer.serialize(this);
    }

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
