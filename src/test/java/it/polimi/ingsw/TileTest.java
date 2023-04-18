package it.polimi.ingsw;

import it.polimi.ingsw.server.model.tile.Tile;
import it.polimi.ingsw.server.model.tile.TileType;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class TileTest {
    @Nested
    @DisplayName("When creating tile")
    class CreationTest {
        @Test
        @DisplayName("type should be equal to assigned type")
        void typeIsEqualToAssigned() {
            TileType type = TileType.CAT;
            Tile tile = new Tile(type);
            assertEquals(tile.getType(), type);
        }
    }
}