package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.server.model.bag.Bag;
import it.polimi.ingsw.server.model.goals.personal.PersonalGoalCard;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.model.turn.TurnManager;
import it.polimi.ingsw.server.model.tile.GameTile;
import it.polimi.ingsw.server.model.player.IPlayer;
import it.polimi.ingsw.server.model.bookshelf.IBookshelf;

import it.polimi.ingsw.server.model.turn.ITurnManager;
import it.polimi.ingsw.server.model.turn.ITurnManagerBuilder;
import it.polimi.ingsw.util.serialization.Deserializer;
import it.polimi.ingsw.util.serialization.JsonDeserializer;
import it.polimi.ingsw.util.serialization.JsonSerializer;
import it.polimi.ingsw.util.serialization.Serializer;
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

        @Override
        public void setBookshelf(IBookshelf bookshelf) {}

        @Override
        public void setPersonalGoalCard(PersonalGoalCard personalGoalCard) {}

        @Override
        public PersonalGoalCard getPersonalGoalCard() { return null; }

        @Override
        public void addPointsToScore(int points) {}

        @Override
        public String serialize(Serializer serializer) {
            return null;
        }
    }

    private record TestBookshelf(boolean full) implements IBookshelf {

        @Override
            public GameTile getTileAt(int row, int column) {
                return null;
            }

            @Override
            public int getWidth() {
                return 0;
            }

            @Override
            public int getHeight() {
                return 0;
            }

        @Override
            public void dropTiles(List<GameTile> tilesToDrop, int column) {
        }

        @Override
            public boolean canDropTiles(int numOfTiles, int column) {
            return false;
        }

        @Override
            public boolean hasTile(int row, int column) {
            return false;
        }

        @Override
            public boolean compareTiles(int row, int column, int row2, int column2) {
            return false;
        }

            @Override
            public List<GameTile> decideTilesOrder(List<GameTile> tilesToDrop, int position1, int position2, int position3) {
                return null;
            }

            @Override
            public String serialize(Serializer serializer) {
                return null;
            }
        }

    private static List<IPlayer> players;

    @BeforeAll
    public static void initPlayers() {
        players = new ArrayList<>();
        players.add(new TestPlayer("a", new TestBookshelf(false)));
        players.add(new TestPlayer("b", new TestBookshelf(false)));
        players.add(new TestPlayer("c", new TestBookshelf(false)));
        players.sort(Comparator.comparing(IPlayer::getUsername));
    }

    @Nested
    @DisplayName("When creating a new Turn Manager")
    class TestCreation {

        @Test
        @DisplayName("player order should be a permutation of the given players")
        void playersArePermutation() {
            TurnManager turnManager = new TurnManager(players);
            List<IPlayer> registeredPlayers = turnManager.getPlayersInOrder();

            registeredPlayers.sort(Comparator.comparing(IPlayer::getUsername));

            assertAll(
                    () -> assertEquals(players.size(), registeredPlayers.size()),
                    () -> assertIterableEquals(players, registeredPlayers)
            );
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

            assertAll(
                    () -> assertTrue(turnManager.isLastLap()),
                    () -> assertTrue(turnManager.isGameOver())
            );
        }

        @Test
        @DisplayName("end of lap with multiple players should be game over")
        void endOfLapAfterEndGameConditionMetWithMultiplePlayers() {
            ITurnManager turnManager = new TurnManager.TurnManagerBuilder()
                    .addPlayer(new TestPlayer("a", new TestBookshelf(false)))
                    .addPlayer(new TestPlayer("b", new TestBookshelf(false)))
                    .addPlayer(new TestPlayer("c", new TestBookshelf(true)))
                    .setCurrentTurn(0)
                    .setIsEndGame(false)
                    .build();

            turnManager.nextTurn();
            boolean isLastAfterTurn1 = turnManager.isLastLap();
            boolean isGameOverAfterTurn1 = turnManager.isGameOver();
            turnManager.nextTurn();
            boolean isLastAfterTurn2 = turnManager.isLastLap();
            boolean isGameOverAfterTurn2 = turnManager.isGameOver();
            turnManager.nextTurn();
            boolean isLastAfterTurn3 = turnManager.isLastLap();
            boolean isGameOverAfterTurn3 = turnManager.isGameOver();

            assertAll(
                    () -> assertFalse(isLastAfterTurn1),
                    () -> assertFalse(isGameOverAfterTurn1),
                    () -> assertFalse(isLastAfterTurn2),
                    () -> assertFalse(isGameOverAfterTurn2),
                    () -> assertTrue(isLastAfterTurn3),
                    () -> assertTrue(isGameOverAfterTurn3),
                    () -> assertTrue(turnManager.isLastLap()),
                    () -> assertTrue(turnManager.isGameOver())
            );
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
            List<IPlayer> turns = new ArrayList<>();
            for (int i = 0; i < players.size(); i++) {
                turns.add(turnManager.getCurrentPlayer());
                turnManager.nextTurn();
            }

            assertIterableEquals(turnManager.getPlayersInOrder(), turns);
        }
    }

    @Nested
    @DisplayName("When creating turn manager through builder")
    class TestSetters {

        @Test
        @DisplayName("turns should follow order of players")
        void orderOfPlayersShouldBeAsSpecified() {
            ITurnManagerBuilder turnManagerBuilder = new TurnManager.TurnManagerBuilder();
            players.forEach(turnManagerBuilder::addPlayer);
            ITurnManager turnManager = turnManagerBuilder.build();

            assertIterableEquals(players, turnManager.getPlayersInOrder());
        }

        @Test
        @DisplayName("setting turn should start sequence at the given index")
        void turnShouldMatchWithParameter() {
            ITurnManagerBuilder turnManagerBuilder = new TurnManager.TurnManagerBuilder();
            players.forEach(turnManagerBuilder::addPlayer);

            assertAll(
                    () -> {
                        turnManagerBuilder.setCurrentTurn(2);
                        assertEquals(players.get(2), turnManagerBuilder.build().getCurrentPlayer());
                    },
                    () -> {
                        turnManagerBuilder.setCurrentTurn(1);
                        assertEquals(players.get(1), turnManagerBuilder.build().getCurrentPlayer());
                    },
                    () -> {
                        turnManagerBuilder.setCurrentTurn(0);
                        assertEquals(players.get(0), turnManagerBuilder.build().getCurrentPlayer());
                    }
            );
        }

        @Test
        @DisplayName("setting endgame to true should terminate game")
        void settingEndGameToTrue() {
            ITurnManagerBuilder turnManagerBuilder = new TurnManager.TurnManagerBuilder();
            ITurnManager turnManager = turnManagerBuilder.setIsEndGame(true).build();

            assertTrue(turnManager.isLastLap());
        }

        @Test
        @DisplayName("setting an invalid index should throw an exception")
        void invalidTurnNumberShouldThrowException() {
            ITurnManagerBuilder turnManagerBuilder = new TurnManager.TurnManagerBuilder();
            players.forEach(turnManagerBuilder::addPlayer);

            assertAll(
                    () -> assertThrows(IllegalArgumentException.class, () -> turnManagerBuilder.setCurrentTurn(-1)),
                    () -> assertThrows(IllegalArgumentException.class, () -> turnManagerBuilder.setCurrentTurn(players.size()))
            );
        }

        @Test
        @DisplayName("trying to add a null player should throw an exception")
        void addingNullPlayerShouldThrowException() {
            ITurnManagerBuilder turnManagerBuilder = new TurnManager.TurnManagerBuilder();
            assertThrows(IllegalArgumentException.class, () -> turnManagerBuilder.addPlayer(null));
        }

        @Test
        @DisplayName("exceeding max number of player should throw an exception")
        void exceedingMaxNumberOfPlayersShouldThrowException() {
            ITurnManagerBuilder turnManagerBuilder = new TurnManager.TurnManagerBuilder();
            for (int i = 0; i < 4; i++) {
                turnManagerBuilder.addPlayer(new Player(""));
            }

            assertThrows(IllegalActionException.class, () -> turnManagerBuilder.addPlayer(new Player("")));
        }
    }

    @Nested
    @DisplayName("When serializing/deserializing turn manager")
    class TestSerialization {
        @Test
        @DisplayName("serialization should return correct data")
        void turnManagerSerialization() {
            ITurnManagerBuilder turnManagerBuilder = new TurnManager.TurnManagerBuilder();
            players.forEach(turnManagerBuilder::addPlayer);
            turnManagerBuilder.setCurrentTurn(1);
            ITurnManager turnManager = turnManagerBuilder.build();

            String serializedData = turnManager.serialize(new JsonSerializer());
            String expected = "{\"players_turn\":1,\"players_order\":[\"a\",\"b\",\"c\"],\"is_end_game\":false}";
            assertEquals(expected, serializedData);
        }

        @Test
        @DisplayName("deserialization should set correct data")
        void turnManagerDeserialization() {
            String serializedData = "{\"players_turn\":1,\"players_order\":[\"a\",\"b\"],\"is_end_game\":true,\"players\":[{\"score\":12,\"bookshelf\":[[0,0,0,0,0],[0,0,0,0,0],[0,0,0,0,0],[3,3,3,3,3],[3,3,3,3,3],[3,3,3,3,3]],\"personal_card_id\":2,\"username\":\"a\"},{\"score\":5,\"bookshelf\":[[1,1,1,1,1],[1,1,1,1,1],[1,1,1,1,1],[2,2,2,2,2],[2,2,2,2,2],[2,2,2,2,2]],\"personal_card_id\":1,\"username\":\"b\"}]}";
            Deserializer deserializer = new JsonDeserializer();
            ITurnManager turnManager = deserializer.deserializeTurn(serializedData, new Bag());

            List<IPlayer> players = turnManager.getPlayersInOrder();
            assertAll(
                    () -> assertEquals("a", players.get(0).getUsername()),
                    () -> assertEquals("b", players.get(1).getUsername()),
                    () -> assertEquals(1, players.indexOf(turnManager.getCurrentPlayer())),
                    () -> assertTrue(turnManager.isLastLap())
            );
        }
    }
}
