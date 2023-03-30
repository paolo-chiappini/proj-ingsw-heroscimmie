package it.polimi.ingsw;

import it.polimi.ingsw.model.CommonGoalCard;
import it.polimi.ingsw.model.CommonGoalCardDeck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CommonGoalCardDeckTest {

    ArrayList<CommonGoalCard> myCards;
    CommonGoalCardDeck commonDeck;
    CommonGoalCardDeck commonDeck3Players;
    CommonGoalCardDeck commonDeck4Players;

    @BeforeEach
    void setUp() {
        commonDeck = new CommonGoalCardDeck(2);
        commonDeck3Players = new CommonGoalCardDeck(3);
        commonDeck4Players = new CommonGoalCardDeck(4);
        myCards = new ArrayList<>();
    }

    /**
     * test if the deck has 12 cards
     */
    @Test
    void deckSize() {
        assertEquals(commonDeck.getCommonGoalCards().size(),12);
    }

    /**
     * test the correct order of the points in a game with 2 players
     */
    @Test
    void getPointsCard2Players() {
        myCards=commonDeck.drawCards();
        assertAll(
                ()->assertEquals(myCards.get(0).evaluatePoints(),8),
                ()->assertEquals(myCards.get(0).evaluatePoints(),4),
                ()->assertEquals(myCards.get(1).evaluatePoints(),8),
                ()->assertEquals(myCards.get(1).evaluatePoints(),4)
        );
    }

    /**
     * test the correct order of the points in a game with 3 players
     */
    @Test
    void getPointsCard3Players() {
        myCards=commonDeck3Players.drawCards();
        assertAll(
                ()->assertEquals(myCards.get(0).evaluatePoints(),8),
                ()->assertEquals(myCards.get(0).evaluatePoints(),6),
                ()->assertEquals(myCards.get(0).evaluatePoints(),4)
        );
    }

    /**
     * test the correct order of the points in a game with 4 players
     */
    @Test
    void getPointsCard4Players() {
        myCards=commonDeck4Players.drawCards();
        assertAll(
                ()->assertEquals(myCards.get(0).evaluatePoints(),8),
                ()->assertEquals(myCards.get(0).evaluatePoints(),6),
                ()->assertEquals(myCards.get(0).evaluatePoints(),4),
                ()->assertEquals(myCards.get(0).evaluatePoints(),2)
        );
    }

    /**
     * test the extraction of 2 common goal cards
     */
    @Test
    void testDraw2Cards() {
        myCards= commonDeck.drawCards();
        assertAll(
                ()->assertFalse(myCards.isEmpty()),
                ()->assertEquals(2,myCards.size()),
                ()->assertTrue(myCards.contains(commonDeck.getCard(1)) || myCards.contains(commonDeck.getCard(2)) ||  myCards.contains(commonDeck.getCard(3)) || myCards.contains(commonDeck.getCard(4)) ||
                        myCards.contains(commonDeck.getCard(5)) || myCards.contains(commonDeck.getCard(6)) || myCards.contains(commonDeck.getCard(7)) || myCards.contains(commonDeck.getCard(8)) ||
                        myCards.contains(commonDeck.getCard(9)) || myCards.contains(commonDeck.getCard(10)) || myCards.contains(commonDeck.getCard(11)) || myCards.contains(commonDeck.getCard(12)))
        );
    }
}