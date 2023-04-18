package it.polimi.ingsw;

import it.polimi.ingsw.mock.DynamicTestBookshelf;
import it.polimi.ingsw.server.model.bookshelf.IBookshelf;
import it.polimi.ingsw.server.model.goals.common.CommonGoalCard10;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
@DisplayName("Tests on CommonGoalCard10")
public class CommonGoalCard10Test {

    CommonGoalCard10 card10;

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
        class CanObtainPointsTests {

            @Test
            @DisplayName("should be true with five tiles of the same type forming an X.")
            void canObtainPointWithSameTilesFormingX() {
                int[][] template = new int[][] {
                        {-1, -1, -1, -1, -1},
                        {-1, -1, -1, -1, -1},
                        { 1,  1, -1,  2,  3},
                        { 4,  2,  4,  2,  4},
                        { 3,  4,  3,  2,  2},
                        { 4,  2,  4,  0,  0}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                assertTrue(card10.canObtainPoints(bookshelf));
            }

            @Test
            @DisplayName("should be false with tiles of the same type not forming an X.")
            void canNotObtainPointsNotX() {
                int[][] template = new int[][] {
                        {-1, -1, -1, -1, -1},
                        {-1, -1, -1, -1, -1},
                        { 1,  1,  1,  2,  3},
                        { 4,  2,  1,  2,  4},
                        { 3,  2,  3,  2,  2},
                        { 0,  2,  2,  0,  0}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                assertFalse(card10.canObtainPoints(bookshelf));
            }
        }
    }
}