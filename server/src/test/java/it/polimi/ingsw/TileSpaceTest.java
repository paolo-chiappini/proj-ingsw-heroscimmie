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

        assertTrue(space.canPlaceTile());
    }

    /**
     * Test if tile can be placed with playersNeeded == playersPlaying
     */
    @Test
    public void testCanPlaceTileTrueEqual() {
        int playersNeeded = 0;
        TileSpace space = new TileSpace(playersNeeded, playersNeeded);

        assertTrue(space.canPlaceTile());
    }

    /**
     * Test if tile can be placed with playersNeeded > playersPlaying
     */
    @Test
    public void testCanPlaceTileFalseLower() {
        int playersNeeded = 0;
        int playersPlaying = playersNeeded - 1;
        TileSpace space = new TileSpace(playersNeeded, playersPlaying);

        assertFalse(space.canPlaceTile());
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
     * Test if new tile cannot be stored on already occupied space.
     */
    @Test
    public void testCannotPlaceOnOccupiedSpace() {
        int playersNeeded = 0;
        TileSpace space = new TileSpace(playersNeeded, playersNeeded);
        GameTile tile = new TestTile(TileType.CAT);

        assertTrue(space.canPlaceTile());
        space.setTile(tile);
        assertFalse(space.canPlaceTile());
    }

    /**
     * Test can place on occupied space after removing previous tile.
     */
    @Test
    public void testCanPlaceAfterRemove() {
        int playersNeeded = 0;
        TileSpace space = new TileSpace(playersNeeded, playersNeeded);
        GameTile tile = new TestTile(TileType.CAT);

        assertTrue(space.canPlaceTile());
        space.setTile(tile);
        assertFalse(space.canPlaceTile());
        space.removeTile();
        assertTrue(space.canPlaceTile());
    }

    /**
     * Test if by trying to set tile in a non-empty space does not
     * store new tiles.
     */
    @Test
    public void testSetTileMultipleOnOccupiedSpace() {
        int playersNeeded = 0;
        TileSpace space = new TileSpace(playersNeeded, playersNeeded);
        GameTile firstTile = new TestTile(TileType.CAT);
        space.setTile(firstTile);

        for(TileType type : TileType.values()) {
            if(type == firstTile.getType()) continue;

            GameTile tile = new TestTile(type);
            space.setTile(tile);
            assertEquals(space.getTile(), firstTile);
            assertEquals(space.getTile().getType(), firstTile.getType());
        }
    }

    /**
     * Test setting different tiles on the same space after
     * it's been emptied each time.
     */
    @Test
    public void testSetTileMultipleWithFree() {
        int playersNeeded = 0;
        TileSpace space = new TileSpace(playersNeeded, playersNeeded);

        for(TileType type : TileType.values()) {
            GameTile tile = new TestTile(type);

            assertTrue(space.canPlaceTile());
            space.setTile(tile);
            assertFalse(space.canPlaceTile());

            assertEquals(space.getTile(), tile);
            assertEquals(space.getTile().getType(), tile.getType());

            space.removeTile();
        }

        assertNull(space.getTile());
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

    /**
     * Test if setTile does not place tile if space is not active.
     */
    @Test
    public void testSetOnInactiveTile() {
        int playersNeeded = 0;
        int playersPlaying = playersNeeded - 1;
        TileSpace space = new TileSpace(playersNeeded, playersPlaying);
        GameTile tile = new TestTile(TileType.CAT);

        assertFalse(space.canPlaceTile());

        space.setTile(tile);
        assertNull(space.getTile());
    }
}
