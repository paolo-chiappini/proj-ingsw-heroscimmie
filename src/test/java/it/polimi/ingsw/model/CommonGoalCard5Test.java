package it.polimi.ingsw.model;

import it.polimi.ingsw.mock.DynamicTestBookshelf;
import it.polimi.ingsw.server.model.bookshelf.IBookshelf;
import it.polimi.ingsw.server.model.goals.common.CommonGoalCard5;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
@DisplayName("Tests on CommonGoalCard5")
public class CommonGoalCard5Test {

    CommonGoalCard5 card5;

    @Nested
    @DisplayName("On card creation")
    class CardCreationTests {
        @BeforeEach
        void createNewCard() {
            card5 = new CommonGoalCard5(2);
        }

        @Test
        @DisplayName("the card id should be 5")
        void cardId() {
            assertEquals(5, card5.getId());
        }

        @Nested
        @DisplayName("When checking if the card's goal is achieved")
        class CanObtainPointsTests {

            @Test
            @DisplayName("should be true with 4 tiles of the same type in the 4 corners of the bookshelf.")
            void canObtainPoints4SameTiles4Corners() {
                int[][] template = new int[][] {
                        { 5, -1,  4, -1,  5},
                        { 2, -1,  5, -1,  2},
                        { 0,  1,  3, -1,  4},
                        { 1,  2,  0,  3,  1},
                        { 4,  1,  1,  2,  4},
                        { 5,  0,  2,  4,  5}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                assertTrue(card5.canObtainPoints(bookshelf));
            }

            @Test
            @DisplayName("should be false with tiles of different type in the 4 corners of the bookshelf.")
            void canNotObtainPoints4DifferentTiles4Corners() {
                int[][] template = new int[][] {
                        { 5, -1,  4, -1,  1},
                        { 2, -1,  5, -1,  0},
                        { 0,  1,  3, -1,  4},
                        { 1,  2,  0,  3,  1},
                        { 4,  1,  1,  2,  4},
                        { 3,  0,  2,  4,  2}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                assertFalse(card5.canObtainPoints(bookshelf));
            }

            @Test
            @DisplayName("should be false with less than 4 tiles of the same type in the 4 corners of the bookshelf.")
            void canNotObtainPoints3SameTiles4Corners()  {
                int[][] template = new int[][] {
                        { 5, -1,  4, -1, -1},
                        { 2, -1,  5, -1,  0},
                        { 0,  1,  3, -1,  4},
                        { 1,  2,  0,  3,  1},
                        { 4,  1,  1,  2,  4},
                        { 5,  0,  2,  4,  5}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                assertFalse(card5.canObtainPoints(bookshelf));
            }

            @Test
            @DisplayName("should be false with no tiles in the 4 corners of the bookshelf.")
            void canNotObtainPointsNoTilesInCorners()  {
                int[][] template = new int[][] {
                        {-1, -1,  4, -1, -1},
                        {-1, -1,  5, -1, -1},
                        {-1,  1,  3, -1, -1},
                        {-1,  2,  0,  3, -1},
                        {-1,  1,  1,  2, -1},
                        {-1,  0,  2,  4, -1}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                assertFalse(card5.canObtainPoints(bookshelf));
            }
        }
    }
}