package it.polimi.ingsw;

import it.polimi.ingsw.mock.DynamicTestBookshelf;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.interfaces.IBookshelf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
@DisplayName("Tests on CommonGoalCard9")
public class CommonGoalCard9Test {

    CommonGoalCard9 card9;

    @Nested
    @DisplayName("On card creation")
    class CardCreationTests {
        @BeforeEach
        void createNewCard() {
            card9 = new CommonGoalCard9(2);
        }

        @Test
        @DisplayName("the card id should be 9")
        void cardId() {
            assertEquals(9, card9.getId());
        }

        @Nested
        @DisplayName("When checking if the card's goal is achieved")
        class CanObtainPointsTests {

            @Test
            @DisplayName("should be true with 3 columns each formed by 6 tiles of maximum 3 different types.")
            void canObtainPoints3Columns3DifferentTypes() {
                int[][] template = new int[][] {
                        { 3,  4, -1,  4, -1},
                        { 1,  0, -1,  2, -1},
                        { 2,  4,  3,  4, -1},
                        { 1,  1,  2,  2, -1},
                        { 3,  1,  0,  1,  1},
                        { 2,  0,  1,  1,  1}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                assertTrue(card9.canObtainPoints(bookshelf));
            }

            @Test
            @DisplayName("should be true with 3 columns each formed by 6 tiles of maximum 2 different types.")
            void canObtainPoints3Columns2DifferentTypes()  {
                int[][] template = new int[][] {
                        { 3,  4, -1,  4, -1},
                        { 3,  0, -1,  1, -1},
                        { 2,  4,  3,  4, -1},
                        { 3,  0,  2,  1, -1},
                        { 3,  0,  0,  1,  1},
                        { 2,  0,  1,  1,  1}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                assertTrue(card9.canObtainPoints(bookshelf));
            }

            @Test
            @DisplayName("should be true with 3 columns each formed by 6 tiles of the same type")
            void canObtainPoints3ColumnsSameType()  {
                int[][] template = new int[][] {
                        { 2,  4, -1,  1, -1},
                        { 2,  4, -1,  1, -1},
                        { 2,  4,  3,  1, -1},
                        { 2,  4,  2,  1, -1},
                        { 2,  4,  0,  1,  1},
                        { 2,  4,  1,  1,  1}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                assertTrue(card9.canObtainPoints(bookshelf));
            }

            @Test
            @DisplayName("should be true with more than 3 columns formed by 6 tiles of maximum 3 different types.")
            void canObtainPointsMoreColumns()  {
                int[][] template = new int[][] {
                        { 3,  4, -1,  4,  3},
                        { 1,  0, -1,  2,  2},
                        { 2,  4,  3,  4,  3},
                        { 1,  1,  2,  2,  2},
                        { 3,  1,  0,  1,  1},
                        { 2,  0,  1,  1,  1}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                assertTrue(card9.canObtainPoints(bookshelf));
            }

            @Test
            @DisplayName("should be false with 3 columns each formed by 6 tiles of more than 3 different types.")
            void canNotObtainPoints3Columns4DifferentTypes()  {
                int[][] template = new int[][] {
                        { 3,  4, -1,  4,  3},
                        { 0,  0, -1,  1,  2},
                        { 2,  3,  3,  4,  0},
                        { 1,  3,  2,  0,  2},
                        { 3,  1,  0,  1,  1},
                        { 2,  0,  1,  1,  1}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                assertFalse(card9.canObtainPoints(bookshelf));
            }

            @Test
            @DisplayName("should be false with columns not formed by 6 tiles.")
            void canNotObtainPointsNot6Tiles()  {
                int[][] template = new int[][] {
                        { 3, -1, -1, -1, -1},
                        { 0,  0, -1,  1, -1},
                        { 2,  3,  3,  4,  0},
                        { 1,  3,  2,  0,  2},
                        { 3,  1,  0,  1,  1},
                        { 2,  0,  1,  1,  1}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                assertFalse(card9.canObtainPoints(bookshelf));
            }

            @Test
            @DisplayName("should be false with less than 3 columns formed by 6 tiles of maximum 3 different types.")
            void canNotObtainsPointLessColumns(){
                int[][] template = new int[][] {
                        { 3,  2, -1,  4, -1},
                        { 1,  0, -1,  2, -1},
                        { 2,  4,  3,  4, -1},
                        { 1,  1,  2,  2, -1},
                        { 3,  1,  0,  1,  1},
                        { 2,  0,  1,  1,  1}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                assertFalse(card9.canObtainPoints(bookshelf));
            }
        }
    }
}