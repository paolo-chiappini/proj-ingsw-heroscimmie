package it.polimi.ingsw;

import it.polimi.ingsw.model.interfaces.GameTile;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.model.TileType;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TileTest {
    /**
     * Test getType of Tile
     */
    @Test
    public void testTileCreation() {
        GameTile tile = new Tile(TileType.CAT);
        assertEquals(tile.getType(), TileType.CAT);
    }
}