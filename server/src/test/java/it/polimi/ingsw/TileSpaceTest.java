package it.polimi.ingsw;

import it.polimi.ingsw.model.interfaces.GameTile;
import it.polimi.ingsw.model.TileSpace;
import it.polimi.ingsw.model.TileType;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TileSpaceTest {
    private static class TestTile implements GameTile {
        private final TileType type;
        public TestTile(TileType type) {
            this.type = type;
        }
        @Override
        public TileType getType() {
            return type;
        }
    }

    /**
     * Test if tile can be placed with playersNeeded <= playersPlaying
     */
    @Test
    public void testCanPlaceTileTrueGreater() {
        int playersNeeded = 0;
        int playersPlaying = playersNeeded + 1;
        TileSpace space = new TileSpace(playersNeeded, playersPlaying);

        assertTrue(space.isActive());
    }

    /**
     * Test if tile can be placed with playersNeeded == playersPlaying
     */
    @Test
    public void testCanPlaceTileTrueEqual() {
        int playersNeeded = 0;
        TileSpace space = new TileSpace(playersNeeded, playersNeeded);

        assertTrue(space.isActive());
    }

    /**
     * Test if tile can be placed with playersNeeded > playersPlaying
     */
    @Test
    public void testCanPlaceTileFalseLower() {
        int playersNeeded = 0;
        int playersPlaying = playersNeeded - 1;
        TileSpace space = new TileSpace(playersNeeded, playersPlaying);

        assertFalse(space.isActive());
    }

    /**
     * Test if the correct tile is stored.
     */
    @Test
    public void testSetTileIsEqual() {
        int playersNeeded = 0;
        GameTile tile = new TestTile(TileType.CAT);
        TileSpace space = new TileSpace(playersNeeded, playersNeeded);

        space.setTile(tile);
        assertEquals(space.getTile().getType(), tile.getType());
    }

    /**
     * Test if changing tile, the correct one is stored.
     */
    @Test
    public void testSetTileMultiple() {
        int playersNeeded = 0;
        TileSpace space = new TileSpace(playersNeeded, playersNeeded);

        for (TileType type : TileType.values()) {
            GameTile tile = new TestTile(type);
            space.setTile(tile);

            assertEquals(space.getTile().getType(), tile.getType());
        }
    }

    /**
     * Test if initial value for tile in space is null (no tile)
     */
    @Test
    public void testDefaultTileIsNull() {
        int playersNeeded = 0;
        TileSpace space = new TileSpace(playersNeeded, playersNeeded);

        assertNull(space.getTile());
    }

    /**
     * Test if remove returns the correct tile and stores null.
     */
    @Test
    public void testRemoveTile() {
        int playersNeeded = 0;
        TileSpace space = new TileSpace(playersNeeded, playersNeeded);
        GameTile tile = new TestTile(TileType.CAT);

        space.setTile(tile);
        GameTile removedTile = space.removeTile();
        assertEquals(removedTile.getType(), tile.getType());

        assertNull(space.getTile());
    }
}
