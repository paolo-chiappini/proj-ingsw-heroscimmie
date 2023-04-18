package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.server.model.bookshelf.Bookshelf;
import it.polimi.ingsw.server.model.tile.Tile;
import it.polimi.ingsw.server.model.tile.TileType;
import it.polimi.ingsw.server.model.tile.GameTile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests on Bookshelf")
class BookshelfTest {
    Bookshelf bookshelf;

    @Nested
    @DisplayName("On bookshelf creation")
    class OnBookshelfCreationTests {
        @BeforeEach
        void createBookshelf() {
            bookshelf = new Bookshelf();
        }

        @Test
        @DisplayName("the bookshelf should be 6x5")
        void bookshelfHasCorrectSize() {
            assertAll(
                    () -> assertEquals(6, bookshelf.getHeight()),
                    () -> assertEquals(5, bookshelf.getWidth())
            );
        }

        @Test
        @DisplayName("the bookshelf should be empty")
        void bookshelfEmptyAtBeginning() {
            for (int i = 0; i < bookshelf.getHeight(); i++) {
                for (int j = 0; j < bookshelf.getWidth(); j++) {
                    assertNull(bookshelf.getTileAt(i, j));
                }
            }
        }

        @Nested
        @DisplayName("When adding tiles")
        class AddingTests {
            @Test
            @DisplayName("a shelf should has a tile")
            void hasTile() {
                List<GameTile> tiles = new ArrayList<>();
                tiles.add(new Tile(TileType.BOOK));
                bookshelf.dropTiles(tiles, 3);
                assertTrue(bookshelf.hasTile(5, 3));
                assertFalse(bookshelf.hasTile(3, 3));
            }
            @Test
            @DisplayName("exception should be raised when column >5 is chosen")
            void NotValidColumn6() {
                List<GameTile> tiles = new ArrayList<>();
                tiles.add(new Tile(TileType.BOOK));
                tiles.add((new Tile(TileType.FRAME)));
                assertThrows(IllegalActionException.class,
                        () -> bookshelf.canDropTiles(tiles.size(), 6));
            }
            @Test
            @DisplayName("exception should be raised when the number of tiles inserted is >3")
            void NotValidNumberOfTiles() {
                List<GameTile> tiles = new ArrayList<>();
                tiles.add(new Tile(TileType.BOOK));
                tiles.add(new Tile(TileType.BOOK));
                tiles.add((new Tile(TileType.FRAME)));
                tiles.add((new Tile(TileType.FRAME)));
                assertThrows(IllegalActionException.class,
                        () -> bookshelf.canDropTiles(tiles.size(), 3));
            }
            @Test
            @DisplayName("the number of tiles to insert should be <= availableSpaces")
            void canDropTilesTrue() {
                List<GameTile> tiles = new ArrayList<>();
                tiles.add(new Tile(TileType.CAT));
                tiles.add((new Tile(TileType.PLANT)));
                bookshelf.dropTiles(tiles, 0);
                bookshelf.dropTiles(tiles, 0);
                assertAll(
                        () -> assertTrue(bookshelf.canDropTiles(2, 0)),
                        () -> assertTrue(bookshelf.canDropTiles(1, 0))
                );
            }
            @Test
            @DisplayName("no tiles can be inserted when the number of tiles to insert are > availableSpaces")
            void canDropTilesFalse() {
                List<GameTile> tiles = new ArrayList<>();
                tiles.add(new Tile(TileType.CAT));
                tiles.add((new Tile(TileType.PLANT)));
                bookshelf.dropTiles(tiles, 0);
                bookshelf.dropTiles(tiles, 0);
                assertFalse(bookshelf.canDropTiles(3, 0));
            }
            @Test
            @DisplayName("a group of n tiles should be inserted in the bookshelf")
            void dropTiles() {
                List<GameTile> tiles = new ArrayList<>();
                tiles.add(new Tile(TileType.BOOK));
                tiles.add((new Tile(TileType.FRAME)));
                bookshelf.dropTiles(tiles, 2);
                assertAll(
                        () -> assertEquals(bookshelf.getTileAt(5, 2).getType(), tiles.get(0).getType()),
                        () -> assertEquals(bookshelf.getTileAt(4, 2).getType(), tiles.get(1).getType()),
                        () -> assertNull(bookshelf.getTileAt(3, 2))
                );
            }
            @Test
            @DisplayName("the bookshelf should be full when adding 30 tiles")
            void isFull() {
                assertFalse(bookshelf.isFull());
                List<GameTile> tiles=new ArrayList<>();
                tiles.add(new Tile(TileType.PLANT));
                for(int i=0;i< bookshelf.getHeight();i++)
                {
                    for(int j=0;j<bookshelf.getWidth();j++)
                    {
                        bookshelf.dropTiles(tiles,j);
                    }
                }
                assertTrue(bookshelf.isFull());
            }

            @Test
            @DisplayName("two tiles should be added in the right order")
            void isRightOrder2Tiles() {
                List<GameTile> tiles=new ArrayList<>();
                List<GameTile> orderTiles;
                tiles.add(new Tile(TileType.PLANT));
                tiles.add(new Tile(TileType.FRAME));
                orderTiles=bookshelf.sort(tiles,2,1,0);
                assertAll(
                        ()-> assertEquals(orderTiles.get(0), tiles.get(1)),
                        ()-> assertEquals(orderTiles.get(1), tiles.get(0))
                );
            }

            @Test
            @DisplayName("three tiles should be added in the right order")
            void isRightOrder3Tiles() {
                List<GameTile> tiles=new ArrayList<>();
                List<GameTile> orderTiles;
                tiles.add(new Tile(TileType.PLANT));
                tiles.add(new Tile(TileType.FRAME));
                tiles.add(new Tile(TileType.TOY));
                orderTiles=bookshelf.sort(tiles,3,1,2);
                assertAll(
                        ()-> assertEquals(orderTiles.get(0), tiles.get(2)),
                        ()-> assertEquals(orderTiles.get(1), tiles.get(0)),
                        ()-> assertEquals(orderTiles.get(2), tiles.get(1))
                );
            }
        @Nested
        @DisplayName("When comparing tiles")
        class ComparingTests {
            @Test
            @DisplayName("should be false when tiles are not of the same type")
            void compareTilesFalse() {
                List<GameTile> tiles = new ArrayList<>();
                tiles.add(new Tile(TileType.BOOK));
                tiles.add((new Tile(TileType.FRAME)));
                bookshelf.dropTiles(tiles, 3);
                assertFalse(bookshelf.compareTiles(5, 3, 4, 3));
                assertFalse(bookshelf.compareTiles(0, 3, 1, 3));
            }
            @Test
            @DisplayName("should be true when tiles are of the same type")
            void compareTilesTrue() {
                List<GameTile> tiles = new ArrayList<>();
                tiles.add(new Tile(TileType.CAT));
                tiles.add((new Tile(TileType.CAT)));
                bookshelf.dropTiles(tiles, 2);
                assertTrue(bookshelf.compareTiles(5, 2, 4, 2));
            }
        }
    }
}
}