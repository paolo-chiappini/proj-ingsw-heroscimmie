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
@DisplayName("Tests on CommonGoalCard10")
public class CommonGoalCard10Test {

    CommonGoalCard10 card10;
    Bookshelf bookshelf;

    @Nested
    @DisplayName("On card creation")
    class CardCreationTests {
        @BeforeEach
        void createNewCard() {
            card10 = new CommonGoalCard10(2);
        }

        @Test
        @DisplayName("the card id should be 10")
        void cardId() {
            assertEquals(10, card10.getId());
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
                tiles.add(new Tile(TileType.CAT));
                tiles.add(new Tile(TileType.BOOK));
                tiles.add(new Tile(TileType.CAT));
                tiles1=new ArrayList<>();
                tiles1.add(new Tile(TileType.PLANT));
                tiles1.add(new Tile(TileType.CAT));
                tiles1.add(new Tile(TileType.PLANT));

            }

            @Test
            @DisplayName("should be true with five tiles of the same type forming an X.")
            void canObtainPointWithSameTilesFormingX() {
                bookshelf.dropTiles(tiles,0);
                bookshelf.dropTiles(tiles1,1);
                bookshelf.dropTiles(tiles,2);
                assertTrue(card10.canObtainPoints(bookshelf));
            }

            @Test
            @DisplayName("should be false with tiles of the same type not forming an X.")
            void canNotObtainPointsNotX() {
                bookshelf.dropTiles(tiles1,0);
                bookshelf.dropTiles(tiles,1);
                bookshelf.dropTiles(tiles1,2);
                assertFalse(card10.canObtainPoints(bookshelf));
            }
        }
    }
}