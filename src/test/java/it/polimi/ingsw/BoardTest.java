package it.polimi.ingsw;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.mock.BagMock;
import it.polimi.ingsw.server.model.board.Board;
import it.polimi.ingsw.server.model.tile.GameTile;
import it.polimi.ingsw.server.model.tile.TileType;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests on Board")
public class BoardTest {
    @Nested
    @DisplayName("When creating a new board")
    class SetupTests {
        @Test
        @DisplayName("board size should be 9")
        void boardShouldHaveSize9(){
            Board board = new Board(0);
            assertEquals(9, board.getSize());
        }

        @Test
        @DisplayName("board should be empty")
        void boardShouldBeEmpty(){
            Board board = new Board(4);
            int emptyCellsCount = 0;
            for (int i = 0; i < board.getSize(); i++)
                for (int j = 0; j < board.getSize(); j++) if (board.getTileAt(i, j) == null) emptyCellsCount++;
            assertEquals(board.getSize() * board.getSize(), emptyCellsCount);
        }

        @ParameterizedTest
        @MethodSource("playersCountProvider")
        @DisplayName("board should be full after initial refill with")
        void isBoardFull(int playersCount){
            Board board = new Board(playersCount);
            board.refill(new BagMock(Map.of(TileType.CAT, 100)));
            int[][] boardTemplate = new int[][]{
                    {5, 5, 5, 3, 4, 5, 5, 5, 5},
                    {5, 5, 5, 2, 2, 4, 5, 5, 5},
                    {5, 5, 3, 2, 2, 2, 3, 5, 5},
                    {5, 4, 2, 2, 2, 2, 2, 2, 3},
                    {4, 2, 2, 2, 2, 2, 2, 2, 4},
                    {3, 2, 2, 2, 2, 2, 2, 4, 5},
                    {5, 5, 3, 2, 2, 2, 3, 5, 5},
                    {5, 5, 5, 4, 2, 2, 5, 5, 5},
                    {5, 5, 5, 5, 4, 3, 5, 5, 5}
            };

            for (int i = 0; i < board.getSize(); i++) {
                for (int j = 0; j < board.getSize(); j++) {
                    if (playersCount < boardTemplate[i][j]) assertNull(board.getTileAt(i, j));
                    else assertNotNull(board.getTileAt(i, j));
                }
            }
        }

        static Stream<Arguments> playersCountProvider() {
            return Stream.of(
                    Arguments.of(Named.of("0 players", 0)),
                    Arguments.of(Named.of("1 player", 1)),
                    Arguments.of(Named.of("2 players", 2)),
                    Arguments.of(Named.of("3 players", 3)),
                    Arguments.of(Named.of("4 players", 4))
            );
        }
    }

    @Nested
    @DisplayName("When checking if board needs refill")
    class CheckRefillTests {
        @Test
        @DisplayName("when there are only tiles without any other adjacent tiles")
        void noAdjacentTiles(){
            Board boardBefore = new Board(2);
            Board boardAfter = new Board(2);
            boardBefore.refill(new BagMock(Map.of(TileType.FRAME, 5)));
            boardAfter.refill(new BagMock(Map.of(TileType.FRAME, 5)));
            boardAfter.pickUpTiles(1, 4, 2, 4);
            boardAfter.pickUpTiles(2, 3, 2, 3);

            assertAll(
                    () -> assertFalse(boardBefore.needsRefill()),
                    () -> assertTrue(boardAfter.needsRefill())
            );
        }

        @Test
        @DisplayName("when there are tiles with some other adjacent tiles")
        void withAdjacentTiles() {
            Board boardBefore = new Board(2);
            Board boardAfter = new Board(2);
            boardBefore.refill(new BagMock(Map.of(TileType.FRAME, 5)));
            boardAfter.refill(new BagMock(Map.of(TileType.FRAME, 5)));
            boardAfter.pickUpTiles(1, 4, 1, 4);
            boardAfter.pickUpTiles(2, 3, 2, 3);

            assertAll(
                    () -> assertFalse(boardBefore.needsRefill()),
                    () -> assertFalse(boardAfter.needsRefill())
            );
        }
    }

    @Nested
    @DisplayName("When picking up tiles from the board in a row")
    class PickUpTilesInRowTests {
        @Test
        @DisplayName("pick up a single tile")
        void pickUpSingleTile() {
            Board board = new Board(2);
            board.refill(new BagMock(Map.of(TileType.PLANT, 100)));
            board.pickUpTiles(6,3,6,3);

            GameTile tileOnBoard = board.getTileAt(5, 3);
            List<GameTile> tilesTaken = board.pickUpTiles(5, 3 ,5, 3);
            assertAll(
                    () -> assertEquals(1, tilesTaken.size()),
                    () -> assertEquals(tileOnBoard, tilesTaken.get(0)),
                    () -> assertNull(board.getTileAt(5, 3))
            );
        }

        @Test
        @DisplayName("pick up two tiles")
        void pickUpTwoTiles() {
            Board board = new Board(2);
            board.refill(new BagMock(Map.of(TileType.PLANT, 100)));
            board.pickUpTiles(7,4,7,5);

            List<GameTile> tilesOnBoard = List.of(board.getTileAt(6, 4), board.getTileAt(6, 5));
            List<GameTile> tilesTaken = board.pickUpTiles(6, 4 ,6, 5);
            assertAll(
                    () -> assertEquals(2, tilesTaken.size()),
                    () -> assertIterableEquals(tilesOnBoard, tilesTaken),
                    () -> assertNull(board.getTileAt(6, 4)),
                    () -> assertNull(board.getTileAt(6, 5))
            );
        }

        @Test
        @DisplayName("pick up three tiles")
        void pickUpThreeTiles() {
            Board board = new Board(2);
            board.refill(new BagMock(Map.of(TileType.PLANT, 100)));
            board.pickUpTiles(7,4,7,5);

            List<GameTile> tilesOnBoard = List.of(
                    board.getTileAt(6, 3),
                    board.getTileAt(6, 4),
                    board.getTileAt(6, 5));
            List<GameTile> tilesTaken = board.pickUpTiles(6, 3 ,6, 5);
            assertAll(
                    () -> assertEquals(3, tilesTaken.size()),
                    () -> assertIterableEquals(tilesOnBoard, tilesTaken),
                    () -> assertNull(board.getTileAt(6, 3)),
                    () -> assertNull(board.getTileAt(6, 4)),
                    () -> assertNull(board.getTileAt(6, 5))
            );
        }

        @Test
        @DisplayName("pick up a single (blocked) tile")
        void cannotPickUpSingleTile() {
            Board board = new Board(2);
            board.refill(new BagMock(Map.of(TileType.PLANT, 100)));

            assertAll(
                    () -> assertFalse(board.canPickUpTiles(5, 3, 5, 3)),
                    () -> assertThrows(
                            IllegalActionException.class,
                            () -> board.pickUpTiles(5, 3, 5, 3))
            );
        }

        @Test
        @DisplayName("cannot pick up a two (blocked) tiles")
        void cannotPickUpTwoTiles() {
            Board board = new Board(2);
            board.refill(new BagMock(Map.of(TileType.PLANT, 100)));

            assertAll(
                    () -> assertFalse(board.canPickUpTiles(6, 3, 6, 4)),
                    () -> assertThrows(
                            IllegalActionException.class,
                            () -> board.pickUpTiles(6, 3, 6, 4))
            );
        }

        @Test
        @DisplayName("cannot pick up three (blocked) tiles")
        void cannotPickThreeTiles() {
            Board board = new Board(2);
            board.refill(new BagMock(Map.of(TileType.PLANT, 100)));

            assertAll(
                    () -> assertFalse(board.canPickUpTiles(6, 3, 6, 5)),
                    () -> assertThrows(
                            IllegalActionException.class,
                            () -> board.pickUpTiles(6, 3, 6, 5))
            );
        }

    }

    @Nested
    @DisplayName("When picking up tiles from the board in a column")
    class PickUpTilesInColTests {
        @Test
        @DisplayName("pick up two tiles")
        void pickUpTwoTiles() {
            Board board = new Board(2);
            board.refill(new BagMock(Map.of(TileType.PLANT, 100)));
            board.pickUpTiles(4,1,5,1);

            List<GameTile> tilesOnBoard = List.of(board.getTileAt(4, 2), board.getTileAt(5, 2));
            List<GameTile> tilesTaken = board.pickUpTiles(4, 2 ,5, 2);
            assertAll(
                    () -> assertEquals(2, tilesTaken.size()),
                    () -> assertIterableEquals(tilesOnBoard, tilesTaken),
                    () -> assertNull(board.getTileAt(4, 2)),
                    () -> assertNull(board.getTileAt(5, 2))
            );
        }

        @Test
        @DisplayName("pick up three tiles")
        void pickUpThreeTiles() {
            Board board = new Board(2);
            board.refill(new BagMock(Map.of(TileType.PLANT, 100)));
            board.pickUpTiles(4,1,5,1);

            List<GameTile> tilesOnBoard = List.of(
                    board.getTileAt(3, 2),
                    board.getTileAt(4, 2),
                    board.getTileAt(5, 2));
            List<GameTile> tilesTaken = board.pickUpTiles(3, 2 ,5, 2);
            assertAll(
                    () -> assertEquals(3, tilesTaken.size()),
                    () -> assertIterableEquals(tilesOnBoard, tilesTaken),
                    () -> assertNull(board.getTileAt(3, 2)),
                    () -> assertNull(board.getTileAt(4, 2)),
                    () -> assertNull(board.getTileAt(5, 2))
            );
        }

        @Test
        @DisplayName("cannot pick up a two (blocked) tiles")
        void cannotPickUpTwoTiles() {
            Board board = new Board(2);
            board.refill(new BagMock(Map.of(TileType.PLANT, 100)));

            assertAll(
                    () -> assertFalse(board.canPickUpTiles(3, 6, 4, 6)),
                    () -> assertThrows(
                            IllegalActionException.class,
                            () -> board.pickUpTiles(3, 6, 4, 6))
            );
        }

        @Test
        @DisplayName("cannot pick up three (blocked) tiles")
        void cannotPickThreeTiles() {
            Board board = new Board(2);
            board.refill(new BagMock(Map.of(TileType.PLANT, 100)));

            assertAll(
                    () -> assertFalse(board.canPickUpTiles(3, 6, 5, 6)),
                    () -> assertThrows(
                            IllegalActionException.class,
                            () -> board.pickUpTiles(3, 6, 5, 6))
            );
        }

    }

    @Nested
    @DisplayName("When picking up tiles from the board in non permitted ways")
    class InvalidPickUpTests {

        @Test
        @DisplayName("non straight range should throw an exception")
        void cannotPickUpNonStraightRange() {
            Board board = new Board(2);
            board.refill(new BagMock(Map.of(TileType.TROPHY, 100)));
            assertAll(
                    () -> assertFalse(board.canPickUpTiles(3, 2, 4, 3)),
                    () -> assertThrows(
                            IllegalActionException.class,
                            () -> board.pickUpTiles(3, 2, 4, 3))
            );
        }

        @Test
        @DisplayName("out of bound index should throw an exception")
        void outOfBoundRange() {
            Board board = new Board(2);
            board.refill(new BagMock(Map.of(TileType.TROPHY, 100)));
            assertAll(
                    () -> assertFalse(board.canPickUpTiles(-1, 2, 4, 3)),
                    () -> assertThrows(
                            IllegalActionException.class,
                            () -> board.pickUpTiles(-1, 2, 4, 3))
            );
        }
    }

    @Nested
    @DisplayName("When testing if a tile has a free side")
    class FreeSpacesTest {
        @Test
        @DisplayName("tile with four free sides")
        void tileWithAllFreeSides() {
            Board board = new Board(3);
            board.refill(new BagMock(Map.of(TileType.CAT, 1)));
            assertFalse(board.hasNoFreeSides(0, 3));
        }

        @Test
        @DisplayName("tile with one free side")
        void tileWithOneFreeSides() {
            Board board = new Board(4);
            board.refill(new BagMock(Map.of(TileType.CAT, 5)));
            assertFalse(board.hasNoFreeSides(1, 4));
        }

        @Test
        @DisplayName("tile has no free sides")
        void tileWithNoFreeSides() {
            Board board = new Board(4);
            board.refill(new BagMock(Map.of(TileType.CAT, 100)));
            assertTrue(board.hasNoFreeSides(4, 4));
        }
    }
}
