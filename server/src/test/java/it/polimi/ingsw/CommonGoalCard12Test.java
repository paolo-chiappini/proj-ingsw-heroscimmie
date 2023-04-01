package it.polimi.ingsw;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.interfaces.GameTile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@DisplayName("Tests on CommonGoalCard12")
public class CommonGoalCard12Test {

    private static CommonGoalCard12 card12;
    Bookshelf bookshelf;

    @Nested
    @DisplayName("On card creation")
    class CardCreationTests {
        @BeforeEach
        void createNewCard() {
            card12 = new CommonGoalCard12(2);
        }

        @Test
        @DisplayName("the card id should be 12")
        void cardId() {
            assertEquals(12, card12.getId());
        }

        @Nested
        @DisplayName("When checking if the card's goal is achieved")
        class CanPlaceTests {
            List<GameTile> tiles;
            List<GameTile> tiles1;

            @BeforeEach
            void createNewBookshelf() {
                bookshelf = new Bookshelf();
                tiles = new ArrayList<>();
                tiles1 = new ArrayList<>();
                tiles.add(new Tile(TileType.CAT));
                tiles.add((new Tile(TileType.FRAME)));
                tiles1.add(new Tile(TileType.TOY));
            }

            @Test
            @DisplayName("should be true with five columns of decreasing height, the first column has 6 tiles")
            void canObtainPointWithDiagonal1() {
                bookshelf.dropTiles(tiles, 0);
                bookshelf.dropTiles(tiles, 0);
                bookshelf.dropTiles(tiles, 0);
                bookshelf.dropTiles(tiles, 1);
                bookshelf.dropTiles(tiles, 1);
                bookshelf.dropTiles(tiles1, 1);
                bookshelf.dropTiles(tiles, 2);
                bookshelf.dropTiles(tiles, 2);
                bookshelf.dropTiles(tiles, 3);
                bookshelf.dropTiles(tiles1, 3);
                bookshelf.dropTiles(tiles, 4);
                assertTrue(card12.canObtainPoints(bookshelf));
                bookshelf.dropTiles(tiles1, 4);
                assertFalse(card12.canObtainPoints(bookshelf));
            }

            @Test
            @DisplayName("should be true with five columns of decreasing height, the first column has 5 tiles")
            void canObtainPointWithDiagonal2() {
                bookshelf.dropTiles(tiles, 0);
                bookshelf.dropTiles(tiles, 0);
                bookshelf.dropTiles(tiles1, 0);
                bookshelf.dropTiles(tiles, 1);
                bookshelf.dropTiles(tiles, 1);
                bookshelf.dropTiles(tiles1, 2);
                bookshelf.dropTiles(tiles, 2);
                bookshelf.dropTiles(tiles, 3);
                bookshelf.dropTiles(tiles1, 4);
                assertTrue(card12.canObtainPoints(bookshelf));
                bookshelf.dropTiles(tiles1, 3);
                assertFalse(card12.canObtainPoints(bookshelf));
            }

            @Test
            @DisplayName("should be true with five columns of increasing height, the first column has 2 tiles")
            void canObtainPointWithDiagonal3() {
                bookshelf.dropTiles(tiles, 4);
                bookshelf.dropTiles(tiles, 4);
                bookshelf.dropTiles(tiles, 4);
                bookshelf.dropTiles(tiles, 3);
                bookshelf.dropTiles(tiles, 3);
                bookshelf.dropTiles(tiles1, 3);
                bookshelf.dropTiles(tiles, 2);
                bookshelf.dropTiles(tiles, 2);
                bookshelf.dropTiles(tiles, 1);
                bookshelf.dropTiles(tiles1, 1);
                bookshelf.dropTiles(tiles, 0);
                assertTrue(card12.canObtainPoints(bookshelf));
                bookshelf.dropTiles(tiles, 0);
                assertFalse(card12.canObtainPoints(bookshelf));
            }

            @Test
            @DisplayName("should be true with five columns of increasing height, the first column has 1 tile")
            void canObtainPointWithDiagonal4() {
                bookshelf.dropTiles(tiles, 4);
                bookshelf.dropTiles(tiles, 4);
                bookshelf.dropTiles(tiles1, 4);
                bookshelf.dropTiles(tiles, 3);
                bookshelf.dropTiles(tiles, 3);
                bookshelf.dropTiles(tiles1, 2);
                bookshelf.dropTiles(tiles, 2);
                bookshelf.dropTiles(tiles, 1);
                bookshelf.dropTiles(tiles1, 0);
                assertTrue(card12.canObtainPoints(bookshelf));
                bookshelf.dropTiles(tiles1, 2);
                assertFalse(card12.canObtainPoints(bookshelf));
            }

            @Test
            @DisplayName("should be false with columns of different heights.")
            void canNotObtainPoints() {
                for (int i = 0; i < bookshelf.getWidth(); i++) {
                    bookshelf.dropTiles(tiles, i);
                }
                assertFalse(card12.canObtainPoints(bookshelf));
            }
        }
    }
}