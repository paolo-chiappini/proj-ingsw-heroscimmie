package it.polimi.ingsw;

import it.polimi.ingsw.mock.*;
import it.polimi.ingsw.model.Bag;
import it.polimi.ingsw.model.CommonGoalCard;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.interfaces.IBoard;
import it.polimi.ingsw.model.interfaces.IBookshelf;
import it.polimi.ingsw.model.interfaces.IPlayer;
import it.polimi.ingsw.model.interfaces.ITurnManager;
import it.polimi.ingsw.util.serialization.JsonSerializer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Test model serialization")
public class JsonSerializationTest {

    @Test
    @DisplayName("Test board serialization")
    public void testBoardSerialization() {
        IBoard board = new BoardMock(3, new int[][] {
                {0, 2, 3},
                {4, -1, 5},
                {3, 4, 1}
        });

        String serializedData = board.serialize(new JsonSerializer());
        String expected = "[[0,2,3],[4,-1,5],[3,4,1]]";

        assertEquals(expected, serializedData);
    }

    @Test
    @DisplayName("Test bookshelf serialization")
    public void testBookshelfSerialization() {
        IBookshelf bookshelf = new DynamicTestBookshelf(new int[][] {
                {0, 2, 3},
                {4, 0, 5},
                {3, 4, 1},
                {0, 0, 0}
        });

        String serializedData = bookshelf.serialize(new JsonSerializer());
        String expected = "[[0,2,3],[4,0,5],[3,4,1],[0,0,0]]";

        assertEquals(expected, serializedData);
    }

    @Test
    @DisplayName("Test bookshelf serialization")
    public void testTurnManagerSerialization() {
        ITurnManager turnManager = new TurnManagerMock(List.of(
                new PlayerMock("a", 0, new DynamicTestBookshelf(new int[][] {{0}}), null),
                new PlayerMock("b", 0, new DynamicTestBookshelf(new int[][] {{-1}}), null),
                new PlayerMock("c", 0, new DynamicTestBookshelf(new int[][] {{1}}), null)
        ), 1, true);

        String serializedData = turnManager.serialize(new JsonSerializer());
        String expected = "{\"players_turn\":1,\"players_order\":[\"a\",\"b\",\"c\"],\"is_end_game\":true}";

        assertEquals(expected, serializedData);
    }

    @Test
    @DisplayName("Test player serialization")
    public void testPlayerSerialization() {
        IPlayer player = new PlayerMock("test", 21, new DynamicTestBookshelf(new int[][] {{1, 2}}), new PersonalCardMock(3));

        String serializedData = player.serialize(new JsonSerializer());
        String expected = "{\"score\":21,\"bookshelf\":[[1,2]],\"personal_card_id\":3,\"username\":\"test\"}";

        assertEquals(expected, serializedData);
    }

    @Test
    @DisplayName("Test common goal card serialization")
    public void testCommonGoalCardSerialization() {
        List<IPlayer> players = List.of(
                new PlayerMock("a", 0, null, null),
                new PlayerMock("b", 0, null, null)
        );
        CommonGoalCard commonGoalCard = new CommonCardMock(4, 3);

        String serializedData = commonGoalCard.serialize(new JsonSerializer());
        String expected = "{\"valid_players\":[\"a\",\"b\"],\"card_id\":4,\"points\":[4,6,8]}";

        assertEquals(expected, serializedData);
    }

    @Test
    @DisplayName("Test game serialization")
    public void testGameSerialization() {
        Game game = new Game.GameBuilder()
                .setTurnManager(new TurnManagerMock(List.of(
                        new PlayerMock("a", 12, new DynamicTestBookshelf(new int[][] {{0}}), new PersonalCardMock(2)),
                        new PlayerMock("b", 5, new DynamicTestBookshelf(new int[][] {{1}}), new PersonalCardMock(1))
                ), 1, false))
                .setCommonGoalCards(List.of(
                        new CommonCardMock(1, 2), // Missing getter for players
                        new CommonCardMock(12, 2)
                ))
                .setBoard(new BoardMock(2, new int[][] {{0, 1},{5, 2}}))
                .setTilesBag(new Bag())
                .build();

        String serializedData = game.serialize(new JsonSerializer());
        String expected = "{\"players_turn\":1,\"players_order\":[\"a\",\"b\"],\"players\":[{\"score\":12,\"bookshelf\":[[0]],\"personal_card_id\":2,\"username\":\"a\"},{\"score\":5,\"bookshelf\":[[1]],\"personal_card_id\":1,\"username\":\"b\"}],\"common_goals\":[{\"valid_players\":[\"a\"],\"card_id\":1,\"points\":[4]},{\"valid_players\":[\"a\",\"b\"],\"card_id\":12,\"points\":[4,8]}],\"board\":[[0,1],[5,2]],\"is_end_game\":true}";

        assertEquals(expected, serializedData);
    }
}
