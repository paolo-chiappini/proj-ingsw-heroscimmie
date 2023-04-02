package it.polimi.ingsw;

import it.polimi.ingsw.mock.DynamicTestBookshelf;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.interfaces.IBookshelf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
@DisplayName("Tests on CommonGoalCard7")
public class CommonGoalCard7Test {

    CommonGoalCard7 card7;

    @Nested
    @DisplayName("On card creation")
    class CardCreationTests {
        @BeforeEach
        void createNewCard() {
            card7 = new CommonGoalCard7(2);
        }

        @Test
        @DisplayName("the card id should be 7")
        void cardId() {
            assertEquals(7, card7.getId());
        }

        @Nested
        @DisplayName("When checking if the card's goal is achieved")
        class CanObtainPointsTests {

            @Test
            @DisplayName("should be true with 2 groups each containing 4 tiles of the same type in a 2x2 square.")
            void canObtainPoints2Squares() {
                int[][] template = new int[][] {
                        {-1, -1, -1, -1, -1},
                        {-1, -1, -1, -1, -1},
                        { 1,  1,  4,  2,  4},
                        { 1,  1,  2,  2,  1},
                        { 3,  2,  2,  2,  4},
                        { 1,  0,  2,  4,  3}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                assertTrue(card7.canObtainPoints(bookshelf));
            }

            @Test
            @DisplayName("should be true with more than 2 groups each containing 4 tiles of the same type in a 2x2 square.")
            void canObtainPoints3Squares() {
                int[][] template = new int[][] {
                        {-1, -1, -1, -1, -1},
                        {-1, -1, -1, -1, -1},
                        { 1,  1,  4,  2,  4},
                        { 1,  1,  2,  2,  1},
                        { 3,  3,  2,  2,  4},
                        { 3,  3,  2,  4,  3}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                assertTrue(card7.canObtainPoints(bookshelf));
            }

            @Test
            @DisplayName("should be false with less than 2 groups containing 4 tiles of the same type in a 2x2 square.")
            void canNotObtainPoints1Square() {
                int[][] template = new int[][] {
                        {-1, -1, -1, -1, -1},
                        {-1, -1, -1, -1, -1},
                        { 0,  1,  4,  2,  4},
                        { 1,  2,  2,  2,  1},
                        { 3,  1,  2,  2,  4},
                        { 3,  0,  2,  4,  3}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                assertFalse(card7.canObtainPoints(bookshelf));
            }

            @Test
            @DisplayName("should be false with tiles not forming a 2X2 square")
            void canNotObtainPointsNoSquares() {
                int[][] template = new int[][] {
                        {-1, -1, -1, -1, -1},
                        {-1, -1, -1, -1, -1},
                        { 0,  1,  4,  2,  4},
                        { 1,  2,  0,  2,  1},
                        { 3,  1,  1,  2,  4},
                        { 3,  0,  2,  4,  3}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                assertFalse(card7.canObtainPoints(bookshelf));
            }

            @Test
            @DisplayName("should be false with 1 group of tiles of the same type forming a 3X2 rectangle")
            void canNotObtainPointsRectangle() {
                int[][] template = new int[][] {
                        {-1, -1, -1, -1, -1},
                        {-1, -1, -1, -1, -1},
                        { 0,  1,  4,  2,  4},
                        { 2,  2,  2,  4,  1},
                        { 2,  2,  2,  3,  4},
                        { 3,  0,  1,  4,  3}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                assertFalse(card7.canObtainPoints(bookshelf));
            }
        }
    }
}