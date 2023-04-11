package it.polimi.ingsw;

import it.polimi.ingsw.mock.DynamicTestBookshelf;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.interfaces.GameTile;
import it.polimi.ingsw.model.interfaces.IBookshelf;
import it.polimi.ingsw.model.interfaces.IPlayer;
import it.polimi.ingsw.model.interfaces.ITurnManager;
import it.polimi.ingsw.model.interfaces.builders.ITurnManagerBuilder;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests on Game")
public class GameTest {
    private static List<IPlayer> players;

    @BeforeAll
    public static void initPlayers() {
        players = new ArrayList<>();
        players.add(new Player("a"));
        players.add(new Player("b"));
        players.add(new Player("c"));
        players.sort(Comparator.comparing(IPlayer::getUsername));
    }

    @Nested
    @DisplayName("When creating a game object")
    class CreationTests {

        @Test
        @DisplayName("a game cannot be created with less than 2 players")
        void createNewTurnManager() {
            List<IPlayer> singlePlayer = new ArrayList<>();
            singlePlayer.add(new Player("test"));

            assertThrows(IllegalArgumentException.class, () -> new Game(singlePlayer));
        }

        @Test
        @DisplayName("each player should get a different personal goal")
        void assignPersonalGoalCards() {
            Game game = new Game(players);
            List<IPlayer> inGamePlayers = game.getPlayers();

            inGamePlayers.sort(Comparator.comparing(IPlayer::getUsername));

            assertAll(
                    () -> assertIterableEquals(players, inGamePlayers),
                    () -> assertNotNull(inGamePlayers.get(0).getPersonalGoalCard()),
                    () -> assertNotNull(inGamePlayers.get(1).getPersonalGoalCard()),
                    () -> assertNotNull(inGamePlayers.get(2).getPersonalGoalCard()),
                    () -> assertNotEquals(inGamePlayers.get(0).getPersonalGoalCard(), inGamePlayers.get(1).getPersonalGoalCard()),
                    () -> assertNotEquals(inGamePlayers.get(0).getPersonalGoalCard(), inGamePlayers.get(2).getPersonalGoalCard()),
                    () -> assertNotEquals(inGamePlayers.get(1).getPersonalGoalCard(), inGamePlayers.get(2).getPersonalGoalCard())
            );
        }

        @Test
        @DisplayName("two different common goals should be set")
        void assignCommonGoalCards() {
            Game game = new Game(players);
            List<CommonGoalCard> commonGoals = game.getCommonGoals();

            assertAll(
                    () -> assertEquals(2, commonGoals.size()),
                    () -> assertNotNull(commonGoals.get(0)),
                    () -> assertNotNull(commonGoals.get(1)),
                    () -> assertNotEquals(commonGoals.get(0), commonGoals.get(1))
            );
        }
    }

    @Nested
    @DisplayName("When the game is ending")
    class EndGameTests {

        @Test
        @DisplayName("players points should be evaluated")
        void playerPointsEvaluation() {
            Game game = new Game(players);
            game.evaluateFinalScores();
        }

        @Test
        @DisplayName("a winning player should be selected")
        void winningPlayerElection() {
            players.get(0).setBookshelf(new DynamicTestBookshelf(new int[][] {{0}}));
            players.get(1).setBookshelf(new DynamicTestBookshelf(new int[][] {{1}}));
            players.get(2).setBookshelf(new DynamicTestBookshelf(new int[][] {{2}}));

            Game game = new Game(players);
            ITurnManager turnManager = game.getTurnManager();

            while (!turnManager.isGameOver()) {
                turnManager.nextTurn();
            }

            assertEquals(players.get(0), game.getWinner());
        }

        @Test
        @DisplayName("in case of draw the winner should be the furthest from the first player")
        void winningPlayerElectionWithDraw() {
            players.get(0).setBookshelf(new DynamicTestBookshelf(new int[][] {{0}}));
            players.get(1).setBookshelf(new DynamicTestBookshelf(new int[][] {{1}}));
            players.get(2).setBookshelf(new DynamicTestBookshelf(new int[][] {{2}}));
            players.get(0).setPersonalGoalCard(new PersonalGoalCard1());
            players.get(1).setPersonalGoalCard(new PersonalGoalCard2());
            players.get(2).setPersonalGoalCard(new PersonalGoalCard3());

            ITurnManagerBuilder turnBuilder = new TurnManager.TurnManagerBuilder();
            for (IPlayer player : players) turnBuilder.addPlayer(player);
            ITurnManager turnManager = turnBuilder
                    .setIsEndGame(true)
                    .setCurrentTurn(0)
                    .build();

            Game game = new Game.GameBuilder()
                    .setTurnManager(turnManager)
                    .setCommonGoalCards(new CommonGoalCardDeck(players.size()).drawCards())
                    .build();

            while (!turnManager.isGameOver()) {
                turnManager.nextTurn();
            }

            assertEquals(players.get(2), game.getWinner());
        }
    }
}
