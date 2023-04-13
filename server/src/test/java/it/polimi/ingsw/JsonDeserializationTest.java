package it.polimi.ingsw;

import it.polimi.ingsw.mock.*;
import it.polimi.ingsw.model.Bag;
import it.polimi.ingsw.model.CommonGoalCard;
import it.polimi.ingsw.model.TileType;
import it.polimi.ingsw.model.interfaces.IBoard;
import it.polimi.ingsw.model.interfaces.IBookshelf;
import it.polimi.ingsw.model.interfaces.IPlayer;
import it.polimi.ingsw.model.interfaces.ITurnManager;
import it.polimi.ingsw.util.serialization.JsonDeserializer;
import it.polimi.ingsw.util.serialization.JsonSerializer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test model serialization")
public class JsonDeserializationTest {

    @Test
    @DisplayName("Test board deserialization")
    public void testBoardDeserialization() {
        String serializedData = "[[-1,-1,-1,4,-1,-1,-1,-1,-1],[-1,-1,-1,-1,-1,-1,-1,-1,-1],[-1,-1,-1,-1,-1,-1,-1,-1,-1],[-1,-1,-1,-1,-1,-1,-1,-1,-1],[-1,-1,-1,-1,-1,-1,-1,2,-1],[-1,-1,-1,-1,-1,-1,-1,-1,-1],[-1,-1,-1,-1,0,-1,-1,-1,-1],[-1,-1,-1,-1,-1,-1,-1,-1,-1],[-1,-1,-1,-1,-1,-1,-1,-1,-1]]";
        IBoard board = new JsonDeserializer().deserializeBoard(serializedData, 3, new Bag());
        int nulls = 0;

        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                // skip selected indexes
                if ((i == 6 && j == 4) || (i == 4 && j == 7) || (i == 0 && j == 3)) continue;
                if (board.getTileAt(i, j) == null) nulls++;
            }
        }

        int finalNulls = nulls;
        assertAll (
                () -> assertNotNull(board),
                () -> assertEquals(9, board.getSize()),
                () -> assertEquals(TileType.CAT, board.getTileAt(6, 4).getType()),
                () -> assertEquals(TileType.TROPHY, board.getTileAt(4, 7).getType()),
                () -> assertEquals(TileType.FRAME, board.getTileAt(0, 3).getType()),
                () -> assertEquals(78, finalNulls)
        );
    }

    @Test
    @DisplayName("Test bookshelf deserialization")
    public void testBookshelfDeserialization() {
        String serializedData = "[[-1,-1,-1,-1,-1],[-1,-1,-1,-1,-1],[-1,-1,-1,-1,-1],[-1,-1,-1,-1,3],[-1,-1,-1,-1,2],[-1,-1,0,-1,1]]";
        IBookshelf bookshelf = new JsonDeserializer().deserializeBookshelf(serializedData, new Bag());
        int nulls = 0;

        for (int i = 0; i < bookshelf.getHeight(); i++) {
            for (int j = 0; j < bookshelf.getWidth(); j++) {
                // skip selected indexes
                if ((i == 5 && j == 4) || (i == 5 && j == 2) || (i == 4 && j == 4) || (i == 3 && j == 4)) continue;
                if (bookshelf.getTileAt(i, j) == null) nulls++;
            }
        }

        int finalNulls = nulls;
        assertAll (
                () -> assertNotNull(bookshelf),
                () -> assertEquals(6, bookshelf.getHeight()),
                () -> assertEquals(5, bookshelf.getWidth()),
                () -> assertEquals(TileType.BOOK, bookshelf.getTileAt(5, 4).getType()),
                () -> assertEquals(TileType.CAT, bookshelf.getTileAt(5, 2).getType()),
                () -> assertEquals(TileType.TROPHY, bookshelf.getTileAt(4, 4).getType()),
                () -> assertEquals(TileType.PLANT, bookshelf.getTileAt(3, 4).getType()),
                () -> assertEquals(26, finalNulls)
        );
    }

    @Test
    @DisplayName("Test bookshelf deserialization")
    public void testTurnManagerDeserialization() {
        String serializedData = "{\"players_turn\":1,\"players_order\":[\"a\",\"b\",\"c\"],\"is_end_game\":true}";
        ITurnManager turnManager = new JsonDeserializer().deserializeTurn(serializedData);
        List<String> usernames = turnManager.getPlayersOrder().stream().map(IPlayer::getUsername).toList();

        assertAll (
                () -> assertNotNull(turnManager),
                () -> assertEquals(3, turnManager.getPlayersOrder().size()),
                () -> assertIterableEquals(List.of("a", "b", "c"), usernames),
                () -> assertEquals("b", turnManager.getCurrentPlayer().getUsername()),
                () -> assertTrue(turnManager.isLastLap())
        );
    }

    @Test
    @DisplayName("Test player deserialization")
    public void testPlayerDeserialization() {
        String serializedData = "{\"score\":21,\"bookshelf\":[[1,2]],\"personal_card_id\":3,\"username\":\"test\"}";
        IPlayer player = new JsonDeserializer().deserializePlayer(serializedData, new Bag());

        assertAll (
                () -> assertNotNull(player),
                () -> assertEquals("test", player.getUsername()),
                () -> assertEquals(21, player.getScore()),
                () -> assertNotNull(player.getBookshelf()),
                () -> assertEquals(3, player.getPersonalGoalCard().getId())
        );
    }

    @Test
    @DisplayName("Test common goal card deserialization")
    public void testCommonGoalCardDeserialization() {
        String serializedData = "{\"valid_players\":[\"a\",\"b\"],\"card_id\":4,\"points\":[4,6,8]}";
        CommonGoalCard commonGoalCard = new JsonDeserializer().deserializeCommonGoalCard(serializedData);

        assertAll (
                () -> assertNotNull(commonGoalCard),
                () -> assertEquals(4, commonGoalCard.getId()),
                () -> assertIterableEquals(List.of(4, 6, 8), commonGoalCard.getPoints()),
                () -> assertIterableEquals(List.of("a", "b"), null) // missing getter for players
        );
    }
}