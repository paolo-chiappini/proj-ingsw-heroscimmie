package it.polimi.ingsw;

import it.polimi.ingsw.model.Bookshelf;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.model.TileType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookshelfTest {
    Bookshelf bookshelf;

    @BeforeEach
    void setUp() {
        bookshelf= new Bookshelf();
    }

    /**
     * test if the tiles are inserted in the library
     */
    @Test
    void dropTiles() {
        List<Tile> tiles =new ArrayList<>();
        tiles.add(new Tile(TileType.BOOK));
        tiles.add((new Tile(TileType.FRAME)));
        bookshelf.dropTiles(tiles,2);
        assertAll(
                () -> assertEquals(bookshelf.getTiles()[5][2].getType(),tiles.get(0).getType()),
                () -> assertEquals(bookshelf.getTiles()[4][2].getType(),tiles.get(1).getType()),
                () -> assertNull(bookshelf.getTiles()[3][2])
        );
    }

    /**
     * Test if tiles can be placed with numOfTiles <= availableSpaces
     */
    @Test
    void canDropTilesTrue() {
        assertTrue(bookshelf.canDropTiles(3,1));
        assertTrue(bookshelf.canDropTiles(6,1));
        List<Tile> tiles =new ArrayList<>();
        tiles.add(new Tile(TileType.CAT));
        tiles.add((new Tile(TileType.PLANT)));
        bookshelf.dropTiles(tiles,0);
        bookshelf.dropTiles(tiles,0);
        assertAll(
                () -> assertTrue(bookshelf.canDropTiles(2,0)),
                ( )-> assertTrue(bookshelf.canDropTiles(1,0))
        );
    }

    /**
     * Test if tiles can be placed with numOfTiles > availableSpaces
     */
    @Test
    void canDropTilesFalse() {
        assertFalse(bookshelf.canDropTiles(7,1));
        List<Tile> tiles =new ArrayList<>();
        tiles.add(new Tile(TileType.CAT));
        tiles.add((new Tile(TileType.PLANT)));
        bookshelf.dropTiles(tiles,0);
        bookshelf.dropTiles(tiles,0);
        assertAll(
                () -> assertFalse(bookshelf.canDropTiles(3,0)),
                () -> assertFalse(bookshelf.canDropTiles(4,0))
        );
    }

    /**
     * Test if the bookshelf is full
     */
    @Test
    void isFull() {
        assertFalse(bookshelf.isFull());
        List<Tile> tiles =new ArrayList<>();
        for(TileType type : TileType.values())
        {
            tiles.add(new Tile(type));
        }
        for(int i=0; i<5; i++)
        {
            bookshelf.dropTiles(tiles,i);
        }
        assertTrue(bookshelf.isFull());
    }

    /**
     * Test if the shelf has a tile
     */
    @Test
    void hasTile() {
        List<Tile> tiles =new ArrayList<>();
        tiles.add(new Tile(TileType.BOOK));
        tiles.add((new Tile(TileType.FRAME)));
        bookshelf.dropTiles(tiles,3);
        assertTrue(bookshelf.hasTile(5,3));
        assertTrue(bookshelf.hasTile(4,3));
        assertFalse(bookshelf.hasTile(3,3));
    }

    /**
     * test if the tiles are not of the same type
     */
    @Test
    void compareTilesFalse() {
        List<Tile> tiles =new ArrayList<>();
        tiles.add(new Tile(TileType.BOOK));
        tiles.add((new Tile(TileType.FRAME)));
        bookshelf.dropTiles(tiles,3);
        assertFalse(bookshelf.compareTiles(5,3,4,3));
        assertFalse(bookshelf.compareTiles(0,3,1,3));
    }

    /**
     * test if the tiles are the same
     */
    @Test
    void compareTilesTrue() {
        List<Tile> tiles =new ArrayList<>();
        tiles.add(new Tile(TileType.CAT));
        tiles.add((new Tile(TileType.CAT)));
        bookshelf.dropTiles(tiles,2);
        assertTrue(bookshelf.compareTiles(5,2,4,2));
    }
}