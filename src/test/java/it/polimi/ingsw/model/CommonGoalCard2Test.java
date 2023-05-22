package it.polimi.ingsw.model;

import it.polimi.ingsw.mock.DynamicTestBookshelf;
import it.polimi.ingsw.server.model.bookshelf.IBookshelf;
import it.polimi.ingsw.server.model.goals.common.CommonGoalCard2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
@DisplayName("Tests on CommonGoalCard2")
public class CommonGoalCard2Test {

    CommonGoalCard2 card2;

    @Nested
    @DisplayName("On card creation")
    class CardCreationTests {
        @BeforeEach
        void createNewCard() {
            card2 = new CommonGoalCard2(2);
        }

        @Test
        @DisplayName("the card id should be 2")
        void cardId() {
            assertEquals(2, card2.getId());
        }

        @Nested
        @DisplayName("When checking if the card's goal is achieved")
        class CanObtainPointsTests {

            @Test
            @DisplayName("should be true with 5 tiles of the same type forming a diagonal.")
            void canObtainPoints5SameTilesDiagonal1() {
                int[][] template = new int[][] {
                        { 5,  3, -1, -1,  1},
                        { 3,  5,  0,  1,  4},
                        { 5,  1,  5,  3,  0},
                        { 4,  1,  2,  5,  4},
                        { 5,  3,  3,  2,  5},
                        { 0,  0,  2,  0,  1}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                assertTrue(card2.canObtainPoints(bookshelf));
            }

            @Test
            @DisplayName("should be true with 5 tiles of the same type forming a diagonal.")
            void canObtainPoints5SameTilesDiagonal2() {
                int[][] template = new int[][] {
                        { 5,  3, -1, -1,  1},
                        { 3,  2,  0,  1,  4},
                        { 5,  3,  5,  3,  0},
                        { 4,  1,  3,  4,  4},
                        { 5,  3,  3,  3,  0},
                        { 0,  0,  2,  0,  3}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                assertTrue(card2.canObtainPoints(bookshelf));
            }

            @Test
            @DisplayName("should be true with 5 tiles of the same type forming a diagonal.")
            void canObtainPoints5SameTilesDiagonal3() {
                int[][] template = new int[][] {
                        { 5,  3, -1, -1,  5},
                        { 3,  2,  0,  5,  4},
                        { 5,  1,  5,  3,  0},
                        { 4,  5,  2,  4,  4},
                        { 5,  3,  3,  2,  0},
                        { 0,  0,  2,  0,  1}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                assertTrue(card2.canObtainPoints(bookshelf));
            }

            @Test
            @DisplayName("should be true with 5 tiles of the same type forming a diagonal.")
            void canObtainPoints5SameTilesDiagonal4() {
                int[][] template = new int[][] {
                        { 5,  3, -1, -1,  1},
                        { 3,  2,  0,  1,  0},
                        { 5,  1,  5,  0,  0},
                        { 4,  1,  0,  4,  4},
                        { 5,  0,  3,  2,  0},
                        { 0,  0,  2,  0,  1}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                assertTrue(card2.canObtainPoints(bookshelf));
            }

            @Test
            @DisplayName("should be false with 4 tiles of the same type forming a diagonal.")
            void canNotObtainPoints4SameTilesDiagonal() {
                int[][] template = new int[][] {
                        { 5,  3, -1, -1,  1},
                        { 3,  2,  0,  1,  4},
                        { 5,  3,  5,  3,  0},
                        { 4,  5,  3,  4,  4},
                        { 5,  3,  5,  3,  0},
                        { 0,  0,  2,  5,  1}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                assertFalse(card2.canObtainPoints(bookshelf));
            }

            @Test
            @DisplayName("should be false with 5 tiles of the same type not forming a diagonal.")
            void canNotObtainPointsNoDiagonal() {
                int[][] template = new int[][] {
                        { 5,  5,  5,  5,  5},
                        { 3,  2,  0,  1,  4},
                        { 5,  1,  5,  3,  0},
                        { 4,  1,  2,  4,  4},
                        { 5,  3,  3,  2,  0},
                        { 0,  0,  2,  0,  1}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                assertFalse(card2.canObtainPoints(bookshelf));
            }
        }
    }
}