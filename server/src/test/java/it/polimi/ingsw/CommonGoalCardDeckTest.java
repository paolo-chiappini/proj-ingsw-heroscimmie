package it.polimi.ingsw;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests on CommonGoalCardDeck")
class CommonGoalCardDeckTest {

    ArrayList<CommonGoalCard> myCards;
    CommonGoalCardDeck commonDeck;
    CommonGoalCardDeck commonDeck3Players;
    CommonGoalCardDeck commonDeck4Players;

    @Nested
    @DisplayName("On common deck creation")
    class OnCommonGoalDeckCreationTests {
        @BeforeEach
        void createDeck() {
            commonDeck = new CommonGoalCardDeck(2);
            commonDeck3Players = new CommonGoalCardDeck(3);
            commonDeck4Players = new CommonGoalCardDeck(4);
            myCards = new ArrayList<>();
        }
        @Test
        @DisplayName("the deck should has 12 cards")
        void deckSize() {
            assertEquals(12, commonDeck.getCommonGoalCards().size());
        }

        @Nested
        @DisplayName("When getting points")
        class GettingPointsTests {
            @Test
            @DisplayName("should get first 8 points then 4 with 2 players")
            void getPointsCard2Players() {
                myCards=commonDeck.drawCards();
                assertAll(
                        () -> assertEquals(8,myCards.get(0).evaluatePoints()),
                        () -> assertEquals(4,myCards.get(0).evaluatePoints()),
                        () -> assertEquals(8,myCards.get(1).evaluatePoints()),
                        () -> assertEquals(4,myCards.get(1).evaluatePoints())
                );
            }
            @Test
            @DisplayName("should get first 8 points then 6 and 4 with 3 players")
            void getPointsCard3Players() {
                myCards = commonDeck3Players.drawCards();
                assertAll(
                        () -> assertEquals(8,myCards.get(0).evaluatePoints()),
                        () -> assertEquals(6,myCards.get(0).evaluatePoints()),
                        () -> assertEquals(4,myCards.get(0).evaluatePoints())
                );
            }
            @Test
            @DisplayName("should get first 8 points then 6, 4 and 2 with 4 players")
            void getPointsCard4Players() {
                myCards = commonDeck4Players.drawCards();
                assertAll(
                        () -> assertEquals(8,myCards.get(0).evaluatePoints()),
                        () -> assertEquals(6,myCards.get(0).evaluatePoints()),
                        () -> assertEquals(4,myCards.get(0).evaluatePoints()),
                        () -> assertEquals(2,myCards.get(0).evaluatePoints())
                );
            }
        }

        @Nested
        @DisplayName("When drawing cards")
        class DrawingTests {
            @Test
            @DisplayName("should get 2 cards")
            void draw2Cards() {
                myCards = commonDeck.drawCards();
                assertEquals(2, myCards.size());
            }
            @Test
            @DisplayName("my cards contains two of the cards in the deck")
            void myCards() {
                myCards = commonDeck.drawCards();
                assertTrue(myCards.contains(commonDeck.getCard(1)) || myCards.contains(commonDeck.getCard(2)) ||
                        myCards.contains(commonDeck.getCard(3)) || myCards.contains(commonDeck.getCard(4)) ||
                        myCards.contains(commonDeck.getCard(5)) || myCards.contains(commonDeck.getCard(6)) ||
                        myCards.contains(commonDeck.getCard(7)) || myCards.contains(commonDeck.getCard(8)) ||
                        myCards.contains(commonDeck.getCard(9)) || myCards.contains(commonDeck.getCard(10)) ||
                        myCards.contains(commonDeck.getCard(11)) || myCards.contains(commonDeck.getCard(12)));
            }
        }
    }
}