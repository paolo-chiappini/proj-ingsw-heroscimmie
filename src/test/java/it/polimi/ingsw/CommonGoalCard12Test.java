package it.polimi.ingsw;

import it.polimi.ingsw.mock.DynamicTestBookshelf;
import it.polimi.ingsw.server.model.bookshelf.IBookshelf;
import it.polimi.ingsw.server.model.goals.common.CommonGoalCard12;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
@DisplayName("Tests on CommonGoalCard12")
public class CommonGoalCard12Test {

    private static CommonGoalCard12 card12;

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
        class CanObtainPointsTests {
            @Test
            @DisplayName("should be true with five columns of decreasing height, the first column has 6 tiles")
            void canObtainPointsWithDiagonal1() {
                int[][] template = new int[][] {
                        { 1, -1, -1, -1, -1},
                        { 2,  1, -1, -1, -1},
                        { 0,  2,  3, -1, -1},
                        { 4,  5,  3,  5, -1},
                        { 4,  4,  3,  3,  3},
                        { 4,  4,  4,  3,  3}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                assertTrue(card12.canObtainPoints(bookshelf));
            }

            @Test
            @DisplayName("should be true with five columns of decreasing height, the first column has 5 tiles")
            void canObtainPointsWithDiagonal2() {
                int[][] template = new int[][] {
                        {-1, -1, -1, -1, -1},
                        { 2, -1, -1, -1, -1},
                        { 0,  2, -1, -1, -1},
                        { 4,  5,  3, -1, -1},
                        { 4,  4,  3,  3, -1},
                        { 4,  4,  4,  3,  3}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                assertTrue(card12.canObtainPoints(bookshelf));
            }

            @Test
            @DisplayName("should be true with five columns of increasing height, the first column has 2 tiles")
            void canObtainPointsWithDiagonal3() {
                int[][] template = new int[][] {
                        {-1, -1, -1, -1,  0},
                        {-1, -1, -1,  1,  4},
                        {-1, -1,  1,  2,  1},
                        {-1,  5,  3,  1,  2},
                        { 4,  4,  3,  3,  1},
                        { 4,  4,  4,  3,  3}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                assertTrue(card12.canObtainPoints(bookshelf));
            }

            @Test
            @DisplayName("should be true with five columns of increasing height, the first column has 1 tile")
            void canObtainPointsWithDiagonal4() {
                int[][] template = new int[][] {
                        {-1, -1, -1, -1, -1},
                        {-1, -1, -1, -1,  4},
                        {-1, -1, -1,  2,  1},
                        {-1, -1,  3,  1,  2},
                        {-1,  4,  3,  3,  1},
                        { 4,  4,  4,  3,  3}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                assertTrue(card12.canObtainPoints(bookshelf));
            }

            @Test
            @DisplayName("should be false with columns of different heights.")
            void canNotObtainPointsDifferentHeights() {
                int[][] template = new int[][] {
                        {-1, -1, -1, -1, -1},
                        {-1, -1, -1, -1,  4},
                        {-1, -1,  0, -1,  1},
                        {-1,  1,  3, -1,  2},
                        {-1,  4,  3,  3,  1},
                        { 4,  4,  4,  3,  3}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                assertFalse(card12.canObtainPoints(bookshelf));
            }
        }
    }
}