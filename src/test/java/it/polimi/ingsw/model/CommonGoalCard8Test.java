package it.polimi.ingsw.model;

import it.polimi.ingsw.mock.DynamicTestBookshelf;
import it.polimi.ingsw.server.model.bookshelf.IBookshelf;
import it.polimi.ingsw.server.model.goals.common.CommonGoalCard8;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
@DisplayName("Tests on CommonGoalCard8")
public class CommonGoalCard8Test {

    CommonGoalCard8 card8;

    @Nested
    @DisplayName("On card creation")
    class CardCreationTests {
        @BeforeEach
        void createNewCard() {
            card8 = new CommonGoalCard8(2);
        }

        @Test
        @DisplayName("the card id should be 8")
        void cardId() {
            assertEquals(8, card8.getId());
        }

        @Nested
        @DisplayName("When checking if the card's goal is achieved")
        class CanObtainPointsTests {

            @Test
            @DisplayName("should be true with 2 lines each formed by 5 different types of tiles.")
            void canObtainPoints2Lines5DifferentTypes() {
                int[][] template = new int[][] {
                        {-1, -1, -1, -1, -1},
                        {-1, -1, -1, -1, -1},
                        { 1,  1,  4,  2,  4},
                        { 1,  1,  2,  2,  1},
                        { 3,  2,  0,  1,  4},
                        { 1,  0,  2,  4,  3}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                assertTrue(card8.canObtainPoints(bookshelf));
            }

            @Test
            @DisplayName("should be true with more than 2 lines each formed by 5 different types of tiles.")
            void canObtainPoints3Lines5DifferentTypes() {
                int[][] template = new int[][] {
                        {-1, -1, -1, -1, -1},
                        {-1, -1, -1, -1, -1},
                        { 1,  1,  4,  2,  4},
                        { 1,  0,  2,  3,  4},
                        { 3,  2,  0,  1,  4},
                        { 1,  0,  2,  4,  3}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                assertTrue(card8.canObtainPoints(bookshelf));
            }

            @Test
            @DisplayName("should be false with 2 lines each formed by maximum 4 different types of tiles.")
            void canNotObtainPoints2Lines4DifferentTypes() {
                int[][] template = new int[][] {
                        {-1, -1, -1, -1, -1},
                        { 4,  3, -1,  0, -1},
                        { 1,  1,  4,  2,  4},
                        { 1,  0,  3,  3,  4},
                        { 2,  2,  0,  1,  4},
                        { 0,  0,  2,  4,  3}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                assertFalse(card8.canObtainPoints(bookshelf));
            }

            @Test
            @DisplayName("should be false with less than 2 line formed by 5 different types of tiles.")
            void canNotObtainPoints1Line5DifferentTypes() {
                int[][] template = new int[][] {
                        {-1, -1, -1, -1, -1},
                        { 4,  3, -1,  0, -1},
                        { 1,  1,  4,  2,  4},
                        { 1,  0,  3,  3,  4},
                        { 2,  3,  0,  1,  4},
                        { 0,  0,  2,  4,  3}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                assertFalse(card8.canObtainPoints(bookshelf));
            }

            @Test
            @DisplayName("should be false with lines not formed by 5 tiles.")
            void canNotObtainPointNot5Tiles()  {
                int[][] template = new int[][] {
                        {-1, -1, -1, -1, -1},
                        { 4,  3, -1,  0, -1},
                        { 1,  1,  4,  2, -1},
                        { 1,  0,  3,  3, -1},
                        { 2,  3,  0,  1, -1},
                        { 0,  0,  2,  4, -1}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                assertFalse(card8.canObtainPoints(bookshelf));
            }
        }
    }
}