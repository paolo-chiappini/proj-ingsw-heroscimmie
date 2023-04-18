package it.polimi.ingsw;

import it.polimi.ingsw.mock.DynamicTestBookshelf;
import it.polimi.ingsw.server.model.bookshelf.IBookshelf;
import it.polimi.ingsw.server.model.goals.common.CommonGoalCard6;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
@DisplayName("Tests on CommonGoalCard6")
public class CommonGoalCard6Test {

    CommonGoalCard6 card6;

    @Nested
    @DisplayName("On card creation")
    class CardCreationTests {
        @BeforeEach
        void createNewCard() {
            card6 = new CommonGoalCard6(2);
        }

        @Test
        @DisplayName("the card id should be 6")
        void cardId() {
            assertEquals(6, card6.getId());
        }

        @Nested
        @DisplayName("When checking if the card's goal is achieved")
        class CanObtainPointsTests {

            @Test
            @DisplayName("should be true with 2 columns each formed by 6 different types of tiles.")
            void canObtainPoints2ColumnsAllDifferentTypes() {
                int[][] template = new int[][] {
                        { 5, -1,  4, -1, -1},
                        { 2, -1,  5, -1, -1},
                        { 0,  1,  3, -1,  4},
                        { 1,  2,  0,  3,  1},
                        { 4,  1,  1,  2,  4},
                        { 3,  0,  2,  4,  3}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                assertTrue(card6.canObtainPoints(bookshelf));
            }

            @Test
            @DisplayName("should be true with more than 2 columns each formed by 6 different types of tiles.")
            void canObtainPoints3ColumnsAllDifferentTypes()  {
                int[][] template = new int[][] {
                        { 5, -1,  4,  5, -1},
                        { 2, -1,  5,  0, -1},
                        { 0,  1,  3,  1,  4},
                        { 1,  2,  0,  3,  1},
                        { 4,  1,  1,  2,  4},
                        { 3,  0,  2,  4,  3}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                assertTrue(card6.canObtainPoints(bookshelf));
            }

            @Test
            @DisplayName("should be false with less than 2 column formed by 6 different types of tiles.")
            void canNotObtainPoints1ColumnAllDifferentTypes()  {
                int[][] template = new int[][] {
                        { 5, -1,  4,  5, -1},
                        { 2, -1,  5,  0, -1},
                        { 3,  1,  3,  5,  4},
                        { 2,  5,  0,  0,  1},
                        { 4,  1,  1,  2,  4},
                        { 3,  0,  2,  4,  3}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                assertFalse(card6.canObtainPoints(bookshelf));
            }

            @Test
            @DisplayName("should be false with columns not formed by 6 tiles.")
            void canNotObtainPointsNot6Tiles()  {
                int[][] template = new int[][] {
                        {-1, -1, -1, -1, -1},
                        { 2, -1, -1,  0, -1},
                        { 3,  1,  3,  5,  4},
                        { 2,  5,  0,  0,  1},
                        { 4,  1,  1,  2,  4},
                        { 3,  0,  2,  4,  3}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                assertFalse(card6.canObtainPoints(bookshelf));
            }

            @Test
            @DisplayName("should be false with 2 columns each formed by a number of different types of tiles < 6")
            void canNotObtainPointsSameType(){
                int[][] template = new int[][] {
                        { 1, -1,  0, -1, -1},
                        { 2, -1,  4,  0, -1},
                        { 3,  1,  3,  5,  4},
                        { 2,  5,  0,  0,  1},
                        { 4,  1,  1,  2,  4},
                        { 3,  0,  2,  4,  3}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                assertFalse(card6.canObtainPoints(bookshelf));
            }
        }
    }
}