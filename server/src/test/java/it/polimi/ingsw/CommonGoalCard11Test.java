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
@DisplayName("Tests on CommonGoalCard11")
public class CommonGoalCard11Test {

    CommonGoalCard11 card11;
    Bookshelf bookshelf;

    @Nested
    @DisplayName("On card creation")
    class CardCreationTests {
        @BeforeEach
        void createNewCard() {
            card11 = new CommonGoalCard11(2);
        }

        @Test
        @DisplayName("the card id should be 11")
        void cardId() {
            assertEquals(11, card11.getId());
        }

        @Nested
        @DisplayName("When checking if the card's goal is achieved")
        class CanPlaceTests {
            List<GameTile> tiles;

            @BeforeEach
            void createNewBookshelf() {
                bookshelf = new Bookshelf();
                tiles = new ArrayList<>();
                tiles.add(new Tile(TileType.CAT));
                tiles.add(new Tile(TileType.CAT));
            }

            @Test
            @DisplayName("should be true with a number of tiles of the same type >=8 ")
            void canObtainPointWith8Tiles() {
                bookshelf.dropTiles(tiles,0);
                bookshelf.dropTiles(tiles,1);
                bookshelf.dropTiles(tiles,2);
                bookshelf.dropTiles(tiles,3);
                assertTrue(card11.canObtainPoints(bookshelf));
                bookshelf.dropTiles(tiles,3);
                assertTrue(card11.canObtainPoints(bookshelf));
            }

            @Test
            @DisplayName("should be false with a number of tiles of the same type < 8")
            void canNotObtainPoints() {
                bookshelf.dropTiles(tiles,0);
                bookshelf.dropTiles(tiles,1);
                bookshelf.dropTiles(tiles,2);
                assertFalse(card11.canObtainPoints(bookshelf));
            }
        }
    }
}