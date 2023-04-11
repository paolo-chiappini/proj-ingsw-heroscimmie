package it.polimi.ingsw.model;

import it.polimi.ingsw.model.interfaces.IPlayer;
import it.polimi.ingsw.model.interfaces.ITurnManager;
import it.polimi.ingsw.model.interfaces.IBookshelf;
import it.polimi.ingsw.model.interfaces.builders.IGameBuilder;
import it.polimi.ingsw.util.Serializable;
import it.polimi.ingsw.util.Serializer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Game implements Serializable {
    private final ITurnManager turnManager;
    private List<CommonGoalCard> commonGoals;
    private IPlayer winner;

    public Game(List<IPlayer> players) {
        if (players == null || players.size() < 2) {
            throw new IllegalArgumentException("There should be at least 2 players");
        }
        turnManager = new TurnManager(players);
        assignCommonGoals();
        assignPersonalGoals();
    }

    private Game(GameBuilder builder) {
        this.turnManager = builder.turnManager;
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

    public void declareWinner() {
        if (!turnManager.isGameOver()) return;
        List<IPlayer> players = turnManager.getPlayersOrder();

        IPlayer topPlayer = players.stream()
                .max(Comparator.comparing(IPlayer::getScore))
                .orElse(null);

        List<IPlayer> playersWithHighestScores = players.stream()
                .filter(p -> p.getScore() == topPlayer.getScore())
                .toList();

        winner = playersWithHighestScores.stream()
                .sorted(Comparator.comparingInt(players::indexOf))
                .reduce((first, second) -> second)
                .orElse(null);
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

    @Override
    public String serialize(Serializer serializer) {
        return null;
    }

    public static class GameBuilder implements IGameBuilder {
        private ITurnManager turnManager;
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
    }
}
