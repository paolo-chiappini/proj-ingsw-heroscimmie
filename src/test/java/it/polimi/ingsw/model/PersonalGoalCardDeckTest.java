package it.polimi.ingsw.model;

import it.polimi.ingsw.mock.DynamicTestBookshelf;
import it.polimi.ingsw.server.model.bookshelf.IBookshelf;
import it.polimi.ingsw.server.model.goals.personal.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests on PersonalGoalCardDeck")
class PersonalGoalCardDeckTest {
    PersonalGoalCardDeck deck;
    PersonalGoalCard myCard;


    @Nested
    @DisplayName("On personal deck creation")
    class OnPersonalGoalDeckCreationTests {
        @BeforeEach
        void createDeck() {
            deck = new PersonalGoalCardDeck();
        }

        @Nested
        @DisplayName("When drawing card")
        class DrawingTests {
            @Test
            @DisplayName("should get 1 card")
            void drawCard() {
                myCard = deck.drawCard();
                assertNotNull(myCard);
            }

            @Test
            @DisplayName("Persona goal card 1")
            void drawCard1() {
                int[][] template = new int[][] {
                        { 3,  1,  4,  1,  1},
                        { 3,  3,  2,  3,  0},
                        { 5,  1,  5,  1,  5},
                        { 0,  5,  2,  3,  4},
                        { 4,  2,  2,  4,  2},
                        { 1,  0,  2,  0,  1}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                myCard=new PersonalGoalCard1();
                assertEquals(12,myCard.evaluatePoints(bookshelf));
            }
            @Test
            @DisplayName("Persona goal card 2")
            void drawCard2() {
                int[][] template = new int[][] {
                        { 0,  1,  4,  1,  1},
                        { 3,  3,  2,  3,  0},
                        { 0,  1,  5,  1,  5},
                        { 0,  5,  2,  3,  1},
                        { 4,  2,  2,  2,  2},
                        { 1,  0,  2,  0,  4}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                myCard=new PersonalGoalCard2();
                assertEquals(12,myCard.evaluatePoints(bookshelf));
            }
            @Test
            @DisplayName("Persona goal card 3")
            void drawCard3() {
                int[][] template = new int[][] {
                        { 0,  1,  4,  1,  1},
                        { 4,  3,  2,  5,  0},
                        { 0,  1,  3,  1,  5},
                        { 0,  0,  2,  3,  2},
                        { 4,  2,  2,  2,  2},
                        { 1,  0,  2,  0,  4}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                myCard=new PersonalGoalCard3();
                assertEquals(12,myCard.evaluatePoints(bookshelf));
            }
            @Test
            @DisplayName("Persona goal card 4")
            void drawCard4() {
                int[][] template = new int[][] {
                        { 0,  1,  4,  1,  5},
                        { 4,  3,  2,  5,  0},
                        { 2,  1,  4,  1,  5},
                        { 0,  0,  2,  3,  2},
                        { 4,  1,  0,  2,  2},
                        { 1,  0,  2,  0,  4}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                myCard=new PersonalGoalCard4();
                assertEquals(12,myCard.evaluatePoints(bookshelf));
            }
            @Test
            @DisplayName("Persona goal card 5")
            void drawCard5() {
                int[][] template = new int[][] {
                        { 0,  1,  4,  1,  5},
                        { 4,  2,  2,  5,  0},
                        { 2,  1,  4,  1,  5},
                        { 0,  4,  1,  3,  2},
                        { 4,  1,  0,  2,  3},
                        { 5,  0,  2,  0,  4}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                myCard=new PersonalGoalCard5();
                assertEquals(12,myCard.evaluatePoints(bookshelf));
            }
            @Test
            @DisplayName("Persona goal card 6")
            void drawCard6() {
                int[][] template = new int[][] {
                        { 0,  1,  2,  1,  0},
                        { 4,  2,  2,  5,  0},
                        { 2,  1,  4,  1,  5},
                        { 0,  4,  1,  3,  2},
                        { 4,  5,  0,  4,  3},
                        { 3,  0,  2,  0,  4}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                myCard=new PersonalGoalCard6();
                assertEquals(12,myCard.evaluatePoints(bookshelf));
            }
            @Test
            @DisplayName("Persona goal card 7")
            void drawCard7() {
                int[][] template = new int[][] {
                        { 0,  1,  2,  1,  0},
                        { 4,  2,  2,  4,  0},
                        { 2,  3,  4,  1,  5},
                        { 2,  4,  1,  3,  2},
                        { 4,  5,  0,  4,  5},
                        { 3,  0,  1,  0,  4}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                myCard=new PersonalGoalCard7();
                assertEquals(12,myCard.evaluatePoints(bookshelf));
            }
            @Test
            @DisplayName("Persona goal card 8")
            void drawCard8() {
                int[][] template = new int[][] {
                        { 0,  1,  2,  1,  4},
                        { 4,  0,  2,  4,  0},
                        { 2,  3,  2,  1,  5},
                        { 3,  4,  1,  3,  2},
                        { 4,  5,  0,  1,  5},
                        { 3,  0,  1,  5,  4}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                myCard=new PersonalGoalCard8();
                assertEquals(12,myCard.evaluatePoints(bookshelf));
            }
            @Test
            @DisplayName("Persona goal card 9")
            void drawCard9() {
                int[][] template = new int[][] {
                        { 0,  1,  5,  1,  4},
                        { 4,  0,  2,  4,  0},
                        { 2,  3,  0,  1,  5},
                        { 3,  4,  1,  3,  1},
                        { 4,  2,  0,  1,  3},
                        { 4,  0,  1,  5,  4}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                myCard=new PersonalGoalCard9();
                assertEquals(12,myCard.evaluatePoints(bookshelf));
            }
            @Test
            @DisplayName("Persona goal card 10")
            void drawCard10() {
                int[][] template = new int[][] {
                        { 0,  1,  5,  1,  2},
                        { 4,  5,  2,  4,  0},
                        { 1,  3,  0,  1,  5},
                        { 3,  4,  1,  0,  1},
                        { 4,  4,  0,  1,  3},
                        { 4,  0,  1,  3,  4}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                myCard=new PersonalGoalCard10();
                assertEquals(12,myCard.evaluatePoints(bookshelf));
            }
            @Test
            @DisplayName("Persona goal card 11")
            void drawCard11() {
                int[][] template = new int[][] {
                        { 0,  1,  3,  1,  2},
                        { 4,  1,  2,  4,  0},
                        { 5,  3,  0,  1,  5},
                        { 3,  4,  4,  0,  1},
                        { 4,  4,  0,  1,  0},
                        { 4,  0,  1,  2,  4}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                myCard=new PersonalGoalCard11();
                assertEquals(12,myCard.evaluatePoints(bookshelf));
            }
            @Test
            @DisplayName("Persona goal card 12")
            void drawCard12() {
                int[][] template = new int[][] {
                        { 0,  1,  1,  1,  2},
                        { 4,  3,  2,  4,  0},
                        { 5,  3,  4,  1,  5},
                        { 3,  4,  4,  2,  1},
                        { 4,  4,  0,  1,  5},
                        { 0,  0,  1,  2,  4}
                };
                IBookshelf bookshelf= new DynamicTestBookshelf(template);
                myCard=new PersonalGoalCard12();
                assertEquals(12,myCard.evaluatePoints(bookshelf));
            }
        }
    }
}