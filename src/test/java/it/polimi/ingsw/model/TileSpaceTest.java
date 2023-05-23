package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.server.model.tile.Tile;
import it.polimi.ingsw.server.model.board.TileSpace;
import it.polimi.ingsw.server.model.tile.TileType;
import it.polimi.ingsw.server.model.tile.GameTile;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests on TileSpace")
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

    @Nested
    @DisplayName("On space creation")
    class OnCreationTests {
        @Test
        @DisplayName("the default tile should be null")
        void defaultTileValueShouldBeNull() {
            TileSpace space = new TileSpace(0, 0);
            assertNull(space.getTile());
        }

        @Test
        @DisplayName("when players >= players needed, space should be active")
        void createdSpaceIsActiveWhenEqual() {
            TileSpace space = new TileSpace(0, 0);
            assertTrue(space.canPlaceTile());
        }

        @Test
        @DisplayName("when players < players needed, space should be inactive")
        void createdSpaceIsActive() {
            TileSpace space = new TileSpace(1, 0);
            assertFalse(space.canPlaceTile());
        }
    }

    @Nested
    @DisplayName("When checking if can place tile")
    class CanPlaceTests {
        @Test
        @DisplayName("should be true with active and empty space")
        void canPlaceWhenActiveAndEmpty() {
            TileSpace space = new TileSpace(0, 0);
            assertTrue(space.canPlaceTile());
        }

        @Test
        @DisplayName("should be false with inactive space")
        void cannotPlaceWhenInactive() {
            TileSpace space = new TileSpace(1, 0);
            assertFalse(space.canPlaceTile());
        }

        @Test
        @DisplayName("should be false with active full space")
        void cannotPlaceWhenActiveAndFull() {
            TileSpace space = new TileSpace(0, 0);
            space.setTile(new TestTile(TileType.CAT));

            assertFalse(space.canPlaceTile());
        }
    }

    @Nested
    @DisplayName("When setting tile")
    class SetTests {
        @Test
        @DisplayName("tile should be stored in active space")
        void tileStoredInActiveSpace() {
            TileSpace space = new TileSpace(0, 0);
            GameTile tile = new TestTile(TileType.CAT);

            space.setTile(tile);
            assertEquals(tile, space.getTile());
        }

        @Test
        @DisplayName("exception should be raised when space is inactive")
        void setOnInactive() {
            TileSpace space = new TileSpace(1, 0);

            assertThrows(IllegalActionException.class,
                    () -> space.setTile(new Tile(TileType.BOOK)));
        }

        @Test
        @DisplayName("exception should be raised when space is full")
        void setOnFull() {
            TileSpace space = new TileSpace(0, 0);
            GameTile tile = new TestTile(TileType.CAT);

            space.setTile(tile);
            assertThrows(IllegalActionException.class,
                    () -> space.setTile(new Tile(TileType.BOOK)));
        }
    }

    @Nested
    @DisplayName("When removing tile")
    class RemoveTests {
        @Test
        @DisplayName("stored tile should be returned and space should be empty")
        void removeTileFromSpace() {
            TileSpace space = new TileSpace(0, 0);
            GameTile tile = new TestTile(TileType.BOOK);

            space.setTile(tile);
            GameTile removedTile = space.removeTile();
            assertAll(
                    () -> assertEquals(removedTile, tile),
                    () -> assertNull(space.getTile()),
                    () -> assertTrue(space.canPlaceTile())
            );
        }
    }
}
