package it.polimi.ingsw.model;

import java.util.ArrayList;

public class CommonGoalCardDeck {
    private static final int NUMBER_OF_COMMON_CARD=2;
    private final ArrayList<CommonGoalCard> commonGoalCards;

    public CommonGoalCardDeck(int numPlayer) {
        commonGoalCards=new ArrayList<>();
        /*commonGoalCards.add(new CommonGoalCard1(numPlayer));
        commonGoalCards.add(new CommonGoalCard2(numPlayer));
        commonGoalCards.add(new CommonGoalCard3(numPlayer));
        commonGoalCards.add(new CommonGoalCard4(numPlayer));
        commonGoalCards.add(new CommonGoalCard5(numPlayer));
        commonGoalCards.add(new CommonGoalCard6(numPlayer));
        commonGoalCards.add(new CommonGoalCard7(numPlayer));
        commonGoalCards.add(new CommonGoalCard8(numPlayer));
        commonGoalCards.add(new CommonGoalCard9(numPlayer));
        commonGoalCards.add(new CommonGoalCard10(numPlayer));
        commonGoalCards.add(new CommonGoalCard11(numPlayer));
        commonGoalCards.add(new CommonGoalCard12(numPlayer));*/
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
        return commonGoalCards;
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