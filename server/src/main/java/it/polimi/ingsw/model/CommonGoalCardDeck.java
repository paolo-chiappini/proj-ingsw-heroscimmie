package it.polimi.ingsw.model;

import java.util.ArrayList;

public class CommonGoalCardDeck {
    private static final int NUMBER_OF_COMMON_CARD=2;
    private final ArrayList<CommonGoalCard> commonGoalCards;

    public CommonGoalCardDeck(int numPlayers) {
        commonGoalCards=new ArrayList<>();
        commonGoalCards.add(new CommonGoalCard1(numPlayers));
        commonGoalCards.add(new CommonGoalCard2(numPlayers));
        commonGoalCards.add(new CommonGoalCard3(numPlayers));
        commonGoalCards.add(new CommonGoalCard4(numPlayers));
        commonGoalCards.add(new CommonGoalCard5(numPlayers));
        commonGoalCards.add(new CommonGoalCard6(numPlayers));
        commonGoalCards.add(new CommonGoalCard7(numPlayers));
        commonGoalCards.add(new CommonGoalCard8(numPlayers));
        commonGoalCards.add(new CommonGoalCard9(numPlayers));
        commonGoalCards.add(new CommonGoalCard10(numPlayers));
        commonGoalCards.add(new CommonGoalCard11(numPlayers));
        commonGoalCards.add(new CommonGoalCard12(numPlayers));
    }

    /**
     * @param cardID is the card identification number
     * @return the card with number cardID
     **/
    public CommonGoalCard getCard(int cardID)
    {
        return commonGoalCards.get(cardID-1);
    }

    /**
     * @return the deck of common goal cards
     **/
    public ArrayList<CommonGoalCard> getCommonGoalCards() {
        return new ArrayList<>(commonGoalCards);
    }

    /**
     * Draw 2 common goal cards from the deck
     * @return 2 common goal cards
     **/
    public ArrayList<CommonGoalCard> drawCards(){
        ArrayList<CommonGoalCard> commonCards = new ArrayList<>(NUMBER_OF_COMMON_CARD);
        for(int i = 0; i < NUMBER_OF_COMMON_CARD; ++i) {
            int randomID = (int)(Math.random()*commonGoalCards.size());
            commonCards.add(commonGoalCards.get(randomID));
        }
        return commonCards;
    }
}