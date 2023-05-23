package it.polimi.ingsw.model;

import it.polimi.ingsw.mock.DynamicTestBookshelf;
import it.polimi.ingsw.server.model.goals.common.CommonGoalCard3;
import it.polimi.ingsw.server.model.bookshelf.IBookshelf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
@DisplayName("Tests on CommonGoalCard3")
public class CommonGoalCard3Test {

    CommonGoalCard3 card3;

    @Nested
    @DisplayName("On card creation")
    class CardCreationTests {
        @BeforeEach
        void createNewCard() {
            card3 = new CommonGoalCard3(2);
        }

        @Test
        @DisplayName("the card id should be 3")
        void cardId() {
            assertEquals(3, card3.getId());
        }

        @Nested
        @DisplayName("When checking if the card's goal is achieved")
        class CanObtainPointsTests {

            @Test
            @DisplayName("should be true with 4 groups each containing at least 4 tiles of the same type")
            void canObtainPoints4Groups4Tiles() {
                int[][] template = new int[][] {
                        {-1, -1, -1,  1,  1},
                        { 3,  3,  1,  1,  2},
                        { 5,  5,  5,  4,  4},
                        { 1,  1,  2,  4,  4},
                        { 1,  2,  2,  2,  0},
                        { 1,  0,  5,  0,  1}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                assertTrue(card3.canObtainPoints(bookshelf));
            }

            @Test
            @DisplayName("should be true with 4 groups each containing more than 4 tiles of the same type")
            void canObtainPoints4GroupsMoreTiles()  {
                int[][] template = new int[][] {
                        {-1, -1,  1,  1,  1},
                        { 3,  0,  1,  1,  4},
                        { 5,  1,  5,  4,  4},
                        { 1,  1,  2,  4,  4},
                        { 1,  2,  2,  2,  0},
                        { 1,  0,  2,  0,  1}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                assertTrue(card3.canObtainPoints(bookshelf));
            }

            @Test
            @DisplayName("should be true with more than 4 groups containing at least 4 tiles of the same type")
            void canObtainPointsMoreGroups()  {
                int[][] template = new int[][] {
                        { 3,  3, -1,  1,  1},
                        { 3,  3,  1,  1,  4},
                        { 5,  1,  5,  4,  4},
                        { 1,  1,  2,  4,  4},
                        { 1,  2,  2,  2,  0},
                        { 1,  0,  2,  0,  1}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                assertTrue(card3.canObtainPoints(bookshelf));
            }

            @Test
            @DisplayName("should be false with less than 4 groups containing at least 4 tiles of the same type")
            void canNotObtainPointsLessGroups() {
                int[][] template = new int[][] {
                        { 3,  3, -1,  1,  1},
                        { 3,  3,  1,  1,  4},
                        { 5,  1,  5,  4,  4},
                        { 4,  1,  2,  4,  4},
                        { 5,  3,  3,  2,  0},
                        { 0,  0,  2,  0,  1}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                assertFalse(card3.canObtainPoints(bookshelf));
            }

            @Test
            @DisplayName("should be false with no groups containing at least 4 tiles of the same type")
            void canNotObtainPointsNoGroups() {
                int[][] template = new int[][] {
                        { 5,  3, -1, -1,  1},
                        { 3,  2,  0,  1,  4},
                        { 5,  1,  5,  3,  0},
                        { 4,  1,  2,  4,  4},
                        { 5,  3,  3,  2,  0},
                        { 0,  0,  2,  0,  1}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                assertFalse(card3.canObtainPoints(bookshelf));
            }
        }
    }
}