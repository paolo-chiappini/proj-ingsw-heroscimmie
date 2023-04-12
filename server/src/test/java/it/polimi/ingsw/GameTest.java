package it.polimi.ingsw;

import it.polimi.ingsw.mock.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.interfaces.IBag;
import it.polimi.ingsw.model.interfaces.IBoard;
import it.polimi.ingsw.model.interfaces.IPlayer;
import it.polimi.ingsw.model.interfaces.ITurnManager;
import it.polimi.ingsw.util.serialization.Deserializer;
import it.polimi.ingsw.util.serialization.JsonDeserializer;
import it.polimi.ingsw.util.serialization.JsonSerializer;
import org.junit.jupiter.api.*;

import java.util.*;

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
            ITurnManager turnManager = new TurnManagerMock(players, 0, true);
            singlePlayer.add(new PlayerMock("test", 0, null, null));

            assertThrows(IllegalArgumentException.class, () -> new Game(singlePlayer, turnManager, null, null));
        }

        @Test
        @DisplayName("each player should get a different personal goal")
        void assignPersonalGoalCards() {
            ITurnManager turnManager = new TurnManagerMock(players, 0, true);
            Game game = new Game(players, turnManager, null, null);
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
            Game game = new Game(players, turnManager, null, null);
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
            IPlayer playerWithScore = new PlayerMock("test", 10, null, new PersonalCardMock(0, 4));
            Game game = new Game.GameBuilder()
                    .setTurnManager(new TurnManagerMock(List.of(playerWithScore), 0, true))
                    .setCommonGoalCards(List.of(new CommonCardMock(0,0), new CommonCardMock(0,0)))
                    .build();

            game.evaluateFinalScores();

            assertEquals(14, game.getWinner().getScore());
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
                                    .setCommonGoalCards(List.of(new CommonCardMock(0,0), new CommonCardMock(0,0)))
                                    .build()
                    )
            );
        }
    }

    @Nested
    @DisplayName("When serializing/deserializing game")
    class TestSerialization {
        @Test
        @DisplayName("serialization should return correct data")
        void gameSerialization() {
            IBag bag = new BagMock(Map.of(
                    TileType.CAT, 10,
                    TileType.BOOK, 13
            ));

            IBoard board = new BoardMock(3, new int[][] {
                    {1, 2, 3},
                    {3, 2, 1},
                    {1, 2, 3}
            });

            Game game = new Game.GameBuilder()
                    .setTurnManager(new TurnManagerMock(players, 1, true))
                    .setTilesBag(bag)
                    .setBoard(board)
                    .setCommonGoalCards(List.of(new CommonCardMock(1, 3), new CommonCardMock(8, 3)))
                    .build();

            String serializedData = game.serialize(new JsonSerializer());
            String expected = "{\"players_turn\":1,\"players_order\":[\"a\",\"b\",\"c\"],\"players\":[{\"score\":0,\"bookshelf\":[[0]],\"personal_card_id\":0,\"username\":\"a\"},{\"score\":0,\"bookshelf\":[[1]],\"personal_card_id\":1,\"username\":\"b\"},{\"score\":0,\"bookshelf\":[[2]],\"personal_card_id\":2,\"username\":\"c\"}],\"bag\":{\"tiles_count\":[10,13]},\"common_goals\":[{\"vaid_players\":[\"a\",\"b\",\"c\"],\"card_id\":1,\"points\":[4,6,8]},{\"valid_players\":[\"a\",\"b\",\"c\"],\"card_id\":8,\"points\":[4,6,8]}],\"board\":[[1,2,3],[3,2,1],[1,2,3]],\"is_end_game\":true}";

            assertEquals(expected, serializedData);
        }

        @Test
        @DisplayName("deserialization should set correct data")
        void gameDeserialization() {

        }
    }
}
