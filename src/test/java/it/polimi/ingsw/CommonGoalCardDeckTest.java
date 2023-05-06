package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.server.model.goals.common.CommonGoalCard;
import it.polimi.ingsw.server.model.goals.common.CommonGoalCardDeck;
import it.polimi.ingsw.server.model.player.Player;
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

    Player player1 = new Player("Primo");
    Player player2 = new Player("Secondo");
    Player player3 = new Player("Terzo");
    Player player4 = new Player("Quarto");

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
                        () -> assertEquals(8,myCards.get(0).evaluatePoints(player1)),
                        () -> assertEquals(4,myCards.get(0).evaluatePoints(player2)),
                        () -> assertEquals(8,myCards.get(1).evaluatePoints(player1)),
                        () -> assertEquals(4,myCards.get(1).evaluatePoints(player2))
                );
            }
            @Test
            @DisplayName("should get first 8 points then 6 and 4 with 3 players")
            void getPointsCard3Players() {
                myCards = commonDeck3Players.drawCards();
                assertAll(
                        () -> assertEquals(8,myCards.get(0).evaluatePoints(player1)),
                        () -> assertEquals(6,myCards.get(0).evaluatePoints(player2)),
                        () -> assertEquals(4,myCards.get(0).evaluatePoints(player3))
                );
            }
            @Test
            @DisplayName("should get first 8 points then 6, 4 and 2 with 4 players")
            void getPointsCard4Players() {
                myCards = commonDeck4Players.drawCards();
                assertAll(
                        () -> assertEquals(8,myCards.get(0).evaluatePoints(player1)),
                        () -> assertEquals(6,myCards.get(0).evaluatePoints(player2)),
                        () -> assertEquals(4,myCards.get(0).evaluatePoints(player3)),
                        () -> assertEquals(2,myCards.get(0).evaluatePoints(player4))
                );
            }

            @Test
            @DisplayName("exception should be raised when a player gets points from the same common goal card twice per game")
            void NotValidPlay() {
                myCards=commonDeck.drawCards();
                myCards.get(0).evaluatePoints(player1);
                assertThrows(IllegalActionException.class,() -> myCards.get(0).evaluatePoints(player1));
            }
        }

        @Nested
        @DisplayName("When drawing cards")
        class DrawingTests {
            @Test
            @DisplayName("my cards should contain two of the cards in the deck")
            void draw2Cards() {
                myCards = commonDeck.drawCards();
                assertAll(
                        ()->assertTrue(commonDeck.getCommonGoalCards().stream().anyMatch(myCards::contains)),
                        ()->assertEquals(2, myCards.size())
                );
            }
        }
    }
}