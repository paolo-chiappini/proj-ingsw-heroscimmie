package it.polimi.ingsw;

import it.polimi.ingsw.mock.*;
import it.polimi.ingsw.model.CommonGoalCard;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.interfaces.IPlayer;
import it.polimi.ingsw.model.interfaces.ITurnManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

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
        players.add(new PlayerMock("a", 0, new DynamicTestBookshelf(new int[][] {{0}}), new PersonalCardMock(0)));
        players.add(new PlayerMock("b", 0, new DynamicTestBookshelf(new int[][] {{1}}), new PersonalCardMock(1)));
        players.add(new PlayerMock("c", 0, new DynamicTestBookshelf(new int[][] {{2}}), new PersonalCardMock(2)));
        players.sort(Comparator.comparing(IPlayer::getUsername));
    }

    @Nested
    @DisplayName("When creating a game object")
    class CreationTests {

        @Test
        @DisplayName("a game cannot be created with less than 2 players")
        void createNewTurnManager() {
            List<IPlayer> singlePlayer = new ArrayList<>();
            ITurnManager turnManager = new TurnManagerMock(singlePlayer, 0, true);
            singlePlayer.add(new PlayerMock("test", 0, null, null));

            assertThrows(IllegalArgumentException.class, () -> new Game(turnManager, null, null));
        }

        @Test
        @DisplayName("each player should get a different personal goal")
        void assignPersonalGoalCards() {
            ITurnManager turnManager = new TurnManagerMock(players, 0, true);
            Game game = new Game(turnManager, null, null);
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
            ITurnManager turnManager = new TurnManagerMock(players, 0, true);
            Game game = new Game(turnManager, null, null);
            List<CommonGoalCard> commonGoals = game.getCommonGoals();

            assertAll(
                    () -> assertEquals(2, commonGoals.size()),
                    () -> assertNotNull(commonGoals.get(0)),
                    () -> assertNotNull(commonGoals.get(1)),
                    () -> assertNotEquals(commonGoals.get(0).getId(), commonGoals.get(1).getId())
            );
        }
    }

    @Nested
    @DisplayName("When the game is ending")
    class EndGameTests {

        @Test
        @DisplayName("players points should be evaluated")
        void playerPointsEvaluation() {
            IPlayer playerWithScore = new PlayerMock("test", 10, new DynamicTestBookshelf(new int[][] {{1, 1, 1}}), new PersonalCardMock(0, 4));
            Game game = new Game.GameBuilder()
                    .setTurnManager(new TurnManagerMock(List.of(playerWithScore), 0, true))
                    .setCommonGoalCards(List.of(new CommonCardMock(0,0), new CommonCardMock(0,0)))
                    .build();

            game.evaluateFinalScores();

            // 10 (base) + 4 (personal card) + 2 (adjacency)
            assertEquals(16, game.getWinner().getScore());
        }

        @Test
        @DisplayName("a winning player should be selected")
        void winningPlayerElection() {
            Game game = new Game.GameBuilder()
                    .setCommonGoalCards(List.of(new CommonCardMock(0, 0), new CommonCardMock(0, 0)))
                    .setTurnManager(new TurnManagerMock(List.of(players.get(0)), 0, true))
                    .build();
            ITurnManager turnManager = game.getTurnManager();

            while (!turnManager.isGameOver()) {
                turnManager.nextTurn();
            }

            assertEquals(players.get(0).getUsername(), game.getWinner().getUsername());
        }

        @Test
        @DisplayName("in case of draw the winner should be the furthest from the first player")
        void winningPlayerElectionWithDraw() {
            Game game = new Game.GameBuilder()
                    .setTurnManager(new TurnManagerMock(players, 0, true))
                    .setCommonGoalCards(List.of(new CommonCardMock(0,0), new CommonCardMock(0, 0)))
                    .build();
            ITurnManager turnManager = game.getTurnManager();

            while (!turnManager.isGameOver()) {
                turnManager.nextTurn();
            }

            assertEquals(players.get(2).getUsername(), game.getWinner().getUsername());
        }
    }

    @Nested
    @DisplayName("When creating a new game through a builder")
    class TestBuilder {

        @Test
        @DisplayName("a turn manager should be set")
        void setNewTurnManager() {
            ITurnManager turnManager = new TurnManagerMock(players, 0, false);
            Game game = new Game.GameBuilder()
                    .setTurnManager(turnManager)
                    .setCommonGoalCards(List.of(new CommonCardMock(0,0), new CommonCardMock(0,0)))
                    .build();

            assertEquals(turnManager, game.getTurnManager());
        }

        @Test
        @DisplayName("common goals should be set")
        void setCommonGoalCards() {
            ITurnManager turnManager = new TurnManagerMock(players, 0, false);
            List<CommonGoalCard> commonGoalCards = List.of(
                    new CommonCardMock(1, 2),
                    new CommonCardMock(2, 2)
            );

            Game game = new Game.GameBuilder()
                    .setTurnManager(turnManager)
                    .setCommonGoalCards(commonGoalCards)
                    .build();

            assertIterableEquals(commonGoalCards, game.getCommonGoals());
        }

        @Test
        @DisplayName("an exception should be thrown if common goals aren't exactly 2")
        void commonGoalsShouldBe2() {
            ITurnManager turnManager = new TurnManagerMock(players, 0, false);

            assertAll(
                    () -> assertThrows(
                            IllegalArgumentException.class,
                            ()-> new Game.GameBuilder()
                                    .setTurnManager(turnManager)
                                    .setCommonGoalCards(null)
                                    .build()
                    ),
                    () -> assertThrows(
                            IllegalArgumentException.class,
                            ()-> new Game.GameBuilder()
                                    .setTurnManager(turnManager)
                                    .setCommonGoalCards(List.of(
                                            new CommonCardMock(1,0),
                                            new CommonCardMock(2, 0),
                                            new CommonCardMock(3, 0)
                                    ))
                                    .build()
                    )
            );
        }

        @Test
        @DisplayName("an exception should be thrown if no turn manager is set")
        void turnManagerShouldBeSet() {
            assertAll(
                    () -> assertThrows(
                            IllegalArgumentException.class,
                            ()-> new Game.GameBuilder()
                                    .setTurnManager(null)
                                    .build()
                    )
            );
        }

        @Test
        @DisplayName("an exception should be thrown if no bag is set")
        void tilesBagShouldBeSet() {
            assertAll(
                    () -> assertThrows(
                            IllegalArgumentException.class,
                            ()-> new Game.GameBuilder()
                                    .setTilesBag(null)
                                    .build()
                    )
            );
        }

        @Test
        @DisplayName("an exception should be thrown if no board is set")
        void gameBoardShouldBeSet() {
            assertAll(
                    () -> assertThrows(
                            IllegalArgumentException.class,
                            ()-> new Game.GameBuilder()
                                    .setBoard(null)
                                    .build()
                    )
            );
        }
    }
}
