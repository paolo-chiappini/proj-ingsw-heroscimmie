package it.polimi.ingsw;

import it.polimi.ingsw.mock.DynamicTestBookshelf;
import it.polimi.ingsw.server.model.bookshelf.IBookshelf;
import it.polimi.ingsw.server.model.goals.common.CommonGoalCard1;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
@DisplayName("Tests on CommonGoalCard1")
public class CommonGoalCard1Test {

    CommonGoalCard1 card1;

    @Nested
    @DisplayName("On card creation")
    class CardCreationTests {
        @BeforeEach
        void createNewCard() {
            card1 = new CommonGoalCard1(2);
        }

        @Test
        @DisplayName("the card id should be 1")
        void cardId() {
            assertEquals(1, card1.getId());
        }

        @Nested
        @DisplayName("When checking if the card's goal is achieved")
        class CanObtainPointsTests {

            @Test
            @DisplayName("should be true with 6 groups each containing at least 2 tiles of the same type")
            void canObtainPoints6Groups2Tiles() {
                int[][] template = new int[][] {
                        { 5,  3, -1, -1,  1},
                        { 3,  0,  0,  1,  4},
                        { 5,  1,  5,  3,  0},
                        { 4,  1,  5,  5,  4},
                        { 5,  3,  3,  2,  5},
                        { 0,  0,  2,  2,  1}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                assertTrue(card1.canObtainPoints(bookshelf));
            }

            @Test
            @DisplayName("should be true with 6 groups each containing more than 2 tiles of the same type")
            void canObtainPoints6GroupsMoreTiles()  {
                int[][] template = new int[][] {
                        { 5,  0, -1, -1,  1},
                        { 3,  0,  0,  1,  1},
                        { 1,  1,  5,  3,  0},
                        { 4,  1,  5,  5,  4},
                        { 5,  0,  5,  2,  5},
                        { 0,  0,  2,  2,  1}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                assertTrue(card1.canObtainPoints(bookshelf));
            }

            @Test
            @DisplayName("should be true with more than 6 groups containing at least 2 tiles of the same type")
            void canObtainPointsMoreGroups()  {
                int[][] template = new int[][] {
                        { 5,  5,  2,  2,  1},
                        { 3,  0,  0,  1,  4},
                        { 5,  1,  5,  3,  0},
                        { 4,  1,  5,  5,  4},
                        { 5,  3,  3,  2,  5},
                        { 0,  0,  2,  2,  1}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                assertTrue(card1.canObtainPoints(bookshelf));
            }

            @Test
            @DisplayName("should be false with less than 6 groups containing at least 2 tiles of the same type")
            void canNotObtainPointsLessGroups() {
                int[][] template = new int[][] {
                        { 5,  3, -1, -1,  1},
                        { 3,  0,  0,  1,  4},
                        { 5,  1,  5,  3,  0},
                        { 4,  1,  5,  5,  4},
                        { 5,  2,  3,  2,  5},
                        { 0,  1,  2,  2,  1}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                assertFalse(card1.canObtainPoints(bookshelf));
            }

            @Test
            @DisplayName("should be false with no groups containing at least 2 tiles of the same type")
            void canNotObtainPointsNoGroups() {
                int[][] template = new int[][] {
                        { 5,  3, -1, -1,  1},
                        { 3,  5,  0,  1,  4},
                        { 5,  1,  5,  3,  0},
                        { 4,  0,  2,  5,  4},
                        { 5,  3,  4,  2,  5},
                        { 0,  1,  2,  0,  1}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                assertFalse(card1.canObtainPoints(bookshelf));
            }
        }
    }
}