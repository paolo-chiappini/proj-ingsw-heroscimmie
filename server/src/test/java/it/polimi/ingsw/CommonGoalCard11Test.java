package it.polimi.ingsw;

import it.polimi.ingsw.mock.DynamicTestBookshelf;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.interfaces.IBookshelf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
@DisplayName("Tests on CommonGoalCard11")
public class CommonGoalCard11Test {

    CommonGoalCard11 card11;

    @Nested
    @DisplayName("On card creation")
    class CardCreationTests {
        @BeforeEach
        void createNewCard() {
            card11 = new CommonGoalCard11(2);
        }

        @Test
        @DisplayName("the card id should be 11")
        void cardId() {
            assertEquals(11, card11.getId());
        }

        @Nested
        @DisplayName("When checking if the card's goal is achieved")
        class CanObtainPointsTests {

            @Test
            @DisplayName("should be true with a number of tiles of the same type >=8 ")
            void canObtainPointsWith8Tiles() {
                int[][] template = new int[][] {
                        {-1, -1, -1, -1, -1},
                        {-1, -1, -1, -1, -1},
                        { 1,  1, -1,  2,  3},
                        { 4,  2,  1,  2,  4},
                        { 3,  2,  3,  2,  2},
                        { 0,  2,  2,  0,  0}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                assertTrue(card11.canObtainPoints(bookshelf));
            }

            @Test
            @DisplayName("should be false with a number of tiles of the same type < 8")
            void canNotObtainPoints() {
                int[][] template = new int[][] {
                        {-1, -1, -1, -1, -1},
                        {-1, -1, -1, -1, -1},
                        { 1,  1, -1,  2,  3},
                        { 4,  4,  1,  2,  4},
                        { 3,  2,  3,  2,  2},
                        { 0,  3,  2,  0,  0}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                assertFalse(card11.canObtainPoints(bookshelf));
            }
        }
    }
}