package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.Bag;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.model.TileType;
import it.polimi.ingsw.model.interfaces.GameTile;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests on BagTest")
class BagTest {

    Bag bag;

    @Nested
    @DisplayName("On bag creation")
    class OnBagCreationTests {
        @BeforeEach
        void createBag() {
            bag = new Bag();
        }

        @Test
        @DisplayName("the bag should has 132 tiles inside")
        void bagSizeAtBeginning() {
            int countTiles = 0;
            for (TileType type : bag.getTilesBag().keySet()) {
                countTiles = countTiles + bag.getTilesBag().get(type);
            }
            assertEquals(132,countTiles);
        }
        @Test
        @DisplayName("the bag should has 6 different types of tiles and each type has 22 tiles")
        void bagTypeAtBeginning() {
            assertEquals(6,bag.getTilesBag().size());
            for (TileType type : bag.getTilesBag().keySet()) {
                assertEquals(22,bag.getTilesBag().get(type));
            }
        }

        @Nested
        @DisplayName("When drawing tiles")
        class DrawingTests {
            @Test
            @DisplayName("one tile should be removed from the bag")
            void drawOneTile() {
                GameTile tile = bag.drawTile();
                assertEquals(21,bag.getTilesBag().get(tile.getType()));
            }
            @Test
            @DisplayName("a random number of tiles should be removed from the bag")
            void drawNTiles() {
                int num = (int) (Math.random() * 132);
                int countTiles = 0;
                for (int i = 0; i < num; i++) {
                    bag.drawTile();
                }
                for (TileType type : bag.getTilesBag().keySet()) {
                    countTiles = countTiles + bag.getTilesBag().get(type);
                }
                assertEquals(132 - num,countTiles);
            }
            @Test
            @DisplayName("the bag should be empty by taking all the tiles")
            void drawAllTiles() {
                for (int i = 0; i < 132; i++) {
                    bag.drawTile();
                }
                assertEquals(0,bag.getTilesBag().size());
                assertTrue(bag.getTilesBag().isEmpty());
            }
            @Test
            @DisplayName("exception should be raised when bag is empty")
            void bagFull() {
                drawAllTiles();
                assertThrows(IllegalActionException.class,
                        () -> bag.drawTile());
            }
        }
        @Nested
        @DisplayName("When adding tiles")
        class AddingTests {
            @Test
            @DisplayName("one tile should be adding to the bag")
            void addOneTile() {
                GameTile tile = bag.drawTile();
                bag.addTile(new Tile(tile.getType()));
                assertEquals(22,bag.getTilesBag().get(tile.getType()));
            }
            @Test
            @DisplayName("exception should be raised when bag is full")
            void bagFull() {
                assertThrows(IllegalActionException.class,
                        () -> bag.addTile(new Tile(TileType.BOOK)));
            }
        }
    }
}