package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.server.model.bag.Bag;
import it.polimi.ingsw.server.model.tile.Tile;
import it.polimi.ingsw.server.model.tile.TileType;
import it.polimi.ingsw.server.model.tile.GameTile;
import it.polimi.ingsw.server.model.bag.IBag;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests on Bag")
class BagTest {

    IBag bag;

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
            @DisplayName("a tile of a chosen type should be removed from the bag")
            void drawTileByType() {
                GameTile tile = bag.getTileByType(TileType.TOY);
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
            @DisplayName("all tiles of a chosen type should be removed from the bag")
            void drawAllTilesOfAType() {
                for (int i = 0; i < 22; i++) {
                    bag.getTileByType(TileType.BOOK);
                }
                assertEquals(110,bag.getTilesBag().values().stream().reduce(0, Integer::sum));
            }
            @Test
            @DisplayName("exception should be raised when bag is empty")
            void bagEmpty() {
                drawAllTiles();
                assertThrows(IllegalActionException.class,
                        () -> bag.drawTile());
            }

            @Test
            @DisplayName("exception should be raised when bag is empty")
            void CannotDrawTilesOfAType() {
                for (TileType type: TileType.values())
                    for (int i = 0; i < 22; i++)
                        bag.getTileByType(type);
                assertThrows(IllegalActionException.class,
                        () -> bag.getTileByType(TileType.BOOK));
            }
        }
        @Nested
        @DisplayName("When adding tiles")
        class AddingTests {
            @Test
            @DisplayName("one tile should be added to the bag")
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