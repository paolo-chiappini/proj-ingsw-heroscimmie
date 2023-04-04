package it.polimi.ingsw;

import it.polimi.ingsw.mock.DynamicTestBookshelf;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.interfaces.IBookshelf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
@DisplayName("Tests on CommonGoalCard4")
public class CommonGoalCard4Test {

    CommonGoalCard4 card4;

    @Nested
    @DisplayName("On card creation")
    class CardCreationTests {
        @BeforeEach
        void createNewCard() {
            card4 = new CommonGoalCard4(2);
        }

        @Test
        @DisplayName("the card id should be 4")
        void cardId() {
            assertEquals(4, card4.getId());
        }

        @Nested
        @DisplayName("When checking if the card's goal is achieved")
        class CanObtainPointsTests {

            @Test
            @DisplayName("should be true with 4 lines each formed by 5 tiles of maximum 3 different types.")
            void canObtainPoints4Lines3DifferentTypes() {
                int[][] template = new int[][] {
                        {-1, -1, -1, -1, -1},
                        { 3,  3,  2,  1,  2},
                        { 5,  1,  5,  1,  2},
                        { 0,  1,  2,  3,  4},
                        { 4,  2,  3,  4,  2},
                        { 1,  0,  5,  0,  1}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                assertTrue(card4.canObtainPoints(bookshelf));
            }

            @Test
            @DisplayName("should be true with 4 lines each formed by 5 tiles of maximum 2 different types.")
            void canObtainPoints4Lines2DifferentTypes() {
                int[][] template = new int[][] {
                        {-1, -1, -1, -1, -1},
                        { 3,  3,  2,  3,  2},
                        { 5,  1,  5,  1,  5},
                        { 0,  1,  2,  3,  4},
                        { 4,  2,  2,  4,  2},
                        { 1,  0,  1,  0,  1}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                assertTrue(card4.canObtainPoints(bookshelf));
            }

            @Test
            @DisplayName("should be true with 4 lines each formed by 5 tiles of the same type.")
            void canObtainPoints4LinesSameType() {
                int[][] template = new int[][] {
                        {-1, -1,  4, -1, -1},
                        { 1,  1,  1,  1,  1},
                        { 3,  3,  3,  3,  3},
                        { 1,  2,  0,  3,  1},
                        { 2,  2,  2,  2,  2},
                        { 0,  0,  0,  0,  0}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                assertTrue(card4.canObtainPoints(bookshelf));
            }
            @Test
            @DisplayName("should be true with more than 4 lines each formed by 5 tiles of maximum 3 different types.")
            void canObtainPoints5Lines3DifferentTypes() {
                int[][] template = new int[][] {
                        {-1, -1, -1, -1, -1},
                        { 3,  3,  2,  1,  2},
                        { 5,  1,  5,  1,  2},
                        { 0,  2,  2,  0,  4},
                        { 4,  2,  3,  4,  2},
                        { 1,  0,  5,  0,  1}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                assertTrue(card4.canObtainPoints(bookshelf));
            }

            @Test
            @DisplayName("should be false with 4 lines each formed by 5 tiles of maximum 4 different types.")
            void canNotObtainPoints4DifferentTypes() {
                int[][] template = new int[][] {
                        {-1, -1, -1, -1, -1},
                        { 3,  3,  2,  1,  2},
                        { 5,  4,  5,  1,  2},
                        { 0,  3,  2,  0,  4},
                        { 4,  5,  3,  4,  2},
                        { 1,  2,  5,  0,  1}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                assertFalse(card4.canObtainPoints(bookshelf));
            }

            @Test
            @DisplayName("should be false with less than 4 lines each formed by 5 tiles of maximum 3 different types.")
            void canNotObtainPoints2Lines3DifferentTypes() {
                int[][] template = new int[][] {
                        {-1, -1, -1, -1, -1},
                        { 3,  3,  2,  1,  2},
                        { 5,  4,  5,  1,  2},
                        { 0,  3,  2,  0,  4},
                        { 4,  5,  2,  4,  2},
                        { 1,  2,  0,  0,  1}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                assertFalse(card4.canObtainPoints(bookshelf));
            }

            @Test
            @DisplayName("should be false with lines not formed by 5 tiles.")
            void canNotObtainPointsNot5Tiles()  {
                int[][] template = new int[][] {
                        { 2, -1,  4, -1, -1},
                        { 3, -1,  5, -1, -1},
                        { 4,  1,  3, -1, -1},
                        { 2,  2,  0,  3,  0},
                        { 1,  1,  1,  2,  1},
                        { 1,  0,  2,  4,  1}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                assertFalse(card4.canObtainPoints(bookshelf));
            }
        }
    }
}