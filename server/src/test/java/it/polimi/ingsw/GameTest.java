package it.polimi.ingsw;

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
            IBookshelf bookshelf = new Bookshelf();
            fillBookshelf(bookshelf);
            players.get(0).setBookshelf(bookshelf);
            players.get(1).setBookshelf(new Bookshelf());
            players.get(2).setBookshelf(new Bookshelf());

            Game game = new Game(players);
            ITurnManager turnManager = game.getTurnManager();

            while (!turnManager.isGameOver()) {
                turnManager.nextTurn();
            }

            game.declareWinner();
            assertEquals(players.get(0), game.getWinner());
        }

        @Test
        @DisplayName("in case of draw the winner should be the furthest from the first player")
        void winningPlayerElectionWithDraw() {
            IBookshelf bookshelf1, bookshelf2;
            bookshelf1 = new Bookshelf();
            bookshelf2 = new Bookshelf();

            fillBookshelf(bookshelf1);
            fillBookshelf(bookshelf2);

            players.get(0).setBookshelf(bookshelf1);
            players.get(1).setBookshelf(bookshelf2);
            players.get(2).setBookshelf(new Bookshelf());
            players.get(0).setPersonalGoalCard(new PersonalGoalCard1(players.size()));
            players.get(1).setPersonalGoalCard(new PersonalGoalCard2(players.size()));
            players.get(2).setPersonalGoalCard(new PersonalGoalCard3(players.size()));

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
            game.evaluateFinalScores();
            game.declareWinner();

            assertAll(
                    () -> assertEquals(turnManager, game.getTurnManager()),
                    () -> assertEquals(players.get(2), game.getWinner())
            );
        }

        void fillBookshelf(IBookshelf bookshelf) {
            for (int i = 0; i < bookshelf.getHeight(); i++) {
                for (int j = 0; j < bookshelf.getWidth(); j++) {
                    List<GameTile> tiles = new ArrayList<>();
                    tiles.add(new Tile(TileType.CAT));
                    bookshelf.dropTiles(tiles, j);
                }
            }
        }
    }
}
