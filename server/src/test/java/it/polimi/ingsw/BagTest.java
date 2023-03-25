package it.polimi.ingsw;

import it.polimi.ingsw.model.Bag;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.model.TileType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BagTest {

    Bag bag;
    @BeforeEach
    void setUp() {
        bag = new Bag();
    }

    /**
     * test if the tile is removed from the bag
     */
    @Test
    void drawOneTile() {
        Tile tile=bag.drawTile();
        assertEquals(bag.getTilesBag().get(tile.getType()),21);
    }

    /**
     * test if the bag empties by taking all the tiles
     */
    @Test
    void drawAllTile()
    {
        for(int i=0;i<132;i++)
        {
            bag.drawTile();
        }
        assertEquals(bag.getTilesBag().size(),0);
        assertTrue(bag.getTilesBag().isEmpty());
    }

    /**
     * test if the bag has 132 tiles
     */
    @Test
    void bagSize() {
        int countTiles=0;
        for(TileType type : bag.getTilesBag().keySet())
        {
            countTiles=countTiles+bag.getTilesBag().get(type);
        }
        assertEquals(countTiles,132);
    }

    /**
     * test if the bag has 6 different types of tiles and each type has 22 tiles
     */
    @Test
    void bagType() {
        assertEquals(bag.getTilesBag().size(),6);
        for(TileType type : bag.getTilesBag().keySet())
        {
            assertEquals(bag.getTilesBag().get(type),22);
        }
    }

    /**
     * test the extraction of a random number of tiles
     */
    @Test
    void drawNTiles()
    {
        int num = (int) (Math.random()*132);
        int countTiles=0;
        for(int i=0;i<num;i++)
        {
            bag.drawTile();
        }
        for(TileType type : bag.getTilesBag().keySet())
        {
            countTiles=countTiles+bag.getTilesBag().get(type);
        }
        assertEquals(countTiles,132-num);
    }
}