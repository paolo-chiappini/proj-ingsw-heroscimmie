package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.TurnManager;
import it.polimi.ingsw.model.interfaces.GameTile;
import it.polimi.ingsw.model.interfaces.IPlayer;
import it.polimi.ingsw.model.interfaces.IBookshelf;

import it.polimi.ingsw.util.JsonDeserializer;
import it.polimi.ingsw.util.JsonSerializer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.Stream;

@DisplayName("Test for Turn Manager")
public class TurnManagerTest {
    private static class TestPlayer implements IPlayer {
        private final String username;
        private final IBookshelf bookshelf;

        public TestPlayer(String name, IBookshelf bookshelf) {
            username = name;
            this.bookshelf = bookshelf;
        }

        @Override
        public String getUsername() {
            return username;
        }

        @Override
        public int getScore() { return 0; }

        @Override
        public IBookshelf getBookshelf() { return bookshelf; }
    }

    private static class TestBookshelf implements IBookshelf {
        private final boolean full;

        public TestBookshelf(boolean full) {
            this.full = full;
        }

        @Override
        public boolean isFull() { return full; }
        @Override
        public void dropTiles(List<GameTile> tilesToDrop, int column) {}
        @Override
        public boolean canDropTiles(int numOfTiles, int column) { return false; }
        @Override
        public boolean hasTile(int row, int column) { return false; }
        @Override
        public boolean compareTiles(int row, int column, int row2, int column2) { return false; }
    }

    private static List<IPlayer> players;

    @BeforeAll
    public static void initPlayers() {
        players = new ArrayList<>();
        players.add(new TestPlayer("a", new TestBookshelf(false)));
        players.add(new TestPlayer("b", new TestBookshelf(false)));
        players.add(new TestPlayer("c", new TestBookshelf(false)));
    }

    @Nested
    @DisplayName("When creating a new Turn Manager")
    class TestCreation {

        @Test
        @DisplayName("player order should be a permutation of the given players")
        void playersArePermutation() {
            TurnManager turnManager = new TurnManager(players);
            Set<IPlayer> playersSet = new HashSet<>();

            for (int i = 0; i < players.size(); i++) {
                IPlayer currentPlayer = turnManager.getCurrentPlayer();
                playersSet.add(currentPlayer);
                turnManager.nextTurn();
            }

            // check if all players in Turn Manager are contained in "players"
            for (IPlayer player : playersSet) {
                assertTrue(players.contains(player));
            }

            // check if all players in "players" are contained in Turn Manager
            for (IPlayer player : players) {
                assertTrue(playersSet.contains(player));
            }
        }

        @Test
        @DisplayName("game should not be over")
        void gameIsNotOver() {
            TurnManager turnManager = new TurnManager(players);
            assertFalse(turnManager.isGameOver());
        }
    }

    @Nested
    @DisplayName("When moving on to the next turn")
    class TestNextTurn {
        @ParameterizedTest
        @DisplayName("the player should play again after one lap")
        @MethodSource("playersIndexProvider")
        void samePlayerAfterLap(int playerIndex) {
            final int numberOfLaps = 3;
            List<IPlayer> playersOrder = new ArrayList<>();
            TurnManager turnManager = new TurnManager(players);

            for (int i = 0; i < numberOfLaps; i++) {
                for (int j = 0; j < players.size(); j++) {
                    playersOrder.add(turnManager.getCurrentPlayer());
                    turnManager.nextTurn();
                }
            }

            assertAll(
                    () -> assertEquals(playersOrder.get(playerIndex), playersOrder.get(playerIndex + players.size())),
                    () -> assertEquals(playersOrder.get(playerIndex), playersOrder.get(playerIndex + players.size() * 2))
            );
        }

        static Stream<Arguments> playersIndexProvider() {
            return Stream.of(
                    Arguments.of(Named.of("first player", 0)),
                    Arguments.of(Named.of("second player", 1)),
                    Arguments.of(Named.of("third player", 2))
            );
        }

        @Test
        @DisplayName("end of lap should be game over")
        void endOfLapAfterEndGameConditionMet() {
            List<IPlayer> player = new ArrayList<>();
            player.add(new TestPlayer("a", new TestBookshelf(true)));

            TurnManager turnManager = new TurnManager(player);
            turnManager.nextTurn();

            assertTrue(turnManager.isGameOver());
        }

        @Test
        @DisplayName("after game over, should throw exception")
        void nextTurnAfterGameOverShouldThrowException() {
            List<IPlayer> player = new ArrayList<>();
            player.add(new TestPlayer("", new TestBookshelf(true)));

            TurnManager turnManager = new TurnManager(player);
            turnManager.nextTurn();

            assertAll (
                    () -> assertTrue(turnManager.isGameOver()),
                    () -> assertThrows(IllegalActionException.class, turnManager::nextTurn)
            );
        }

        @Test
        @DisplayName("the sequence of players should be respected")
        void turnSequenceShouldBeRespected() {
            TurnManager turnManager = new TurnManager(players);
            IPlayer p1, p2, p3;
            p1 = turnManager.getCurrentPlayer();
            turnManager.nextTurn();
            p2 = turnManager.getCurrentPlayer();
            turnManager.nextTurn();
            p3 = turnManager.getCurrentPlayer();

            assertAll(
                    () -> assertEquals(turnManager.getPlayersOrder().get(0), p1),
                    () -> assertEquals(turnManager.getPlayersOrder().get(1), p2),
                    () -> assertEquals(turnManager.getPlayersOrder().get(2), p3)
            );
        }
    }

    @Nested
    @DisplayName("When restoring turn manager state through setters")
    class TestSetters {

        @Test
        @DisplayName("turns should follow order of players")
        void orderOfPlayersShouldBeAsSpecified() {
            TurnManager turnManager = new TurnManager();
            turnManager.setPlayersOrder(players);

            for (IPlayer player : players) {
                assertEquals(player, turnManager.getCurrentPlayer());
                turnManager.nextTurn();
            }
        }

        @Test
        @DisplayName("setting turn should start sequence at the given index")
        void turnShouldMatchWithParameter() {
            TurnManager turnManager = new TurnManager();
            turnManager.setPlayersOrder(players);

            assertAll(
                    () -> {
                        turnManager.setCurrentTurn(2);
                        assertEquals(players.get(2), turnManager.getCurrentPlayer());
                    },
                    () -> {
                        turnManager.setCurrentTurn(1);
                        assertEquals(players.get(1), turnManager.getCurrentPlayer());
                    },
                    () -> {
                        turnManager.setCurrentTurn(0);
                        assertEquals(players.get(0), turnManager.getCurrentPlayer());
                    }
            );
        }

        @Test
        @DisplayName("setting an invalid index should throw an exception")
        void invalidTurnNumberShouldThrowException() {
            TurnManager turnManager = new TurnManager();

            assertAll(
                    () -> assertThrows(IllegalArgumentException.class, () -> turnManager.setCurrentTurn(-1)),
                    () -> assertThrows(IllegalArgumentException.class, () -> turnManager.setCurrentTurn(players.size()))
            );
        }
    }

    @Nested
    @DisplayName("When serializing/deserializing turn manager")
    class TestSerialization {
        @Test
        @DisplayName("serialization should return correct data")
        void turnManagerSerialization() {
            TurnManager turnManager = new TurnManager();
            turnManager.setPlayersOrder(players);
            turnManager.setCurrentTurn(1);

            String serializedData = turnManager.serialize(new JsonSerializer());
            String expected = "{\"players_turn\":1,\"players_order\":[\"a\",\"b\",\"c\"]}";
            assertEquals(expected, serializedData);
        }

        @Test
        @DisplayName("deserialization should set correct data")
        void turnManagerDeserialization() {
            TurnManager turnManager = new TurnManager();
            String serializedData = "{\"players_turn\":1,\"players_order\":[\"a\",\"b\",\"c\"]}";

            turnManager.deserialize(new JsonDeserializer(), serializedData);
            List<IPlayer> players = turnManager.getPlayersOrder();
            assertAll(
                    () -> assertEquals("a", players.get(0).getUsername()),
                    () -> assertEquals("b", players.get(1).getUsername()),
                    () -> assertEquals("c", players.get(2).getUsername()),
                    () -> assertEquals(1, players.indexOf(turnManager.getCurrentPlayer()))
            );
        }
    }
}
