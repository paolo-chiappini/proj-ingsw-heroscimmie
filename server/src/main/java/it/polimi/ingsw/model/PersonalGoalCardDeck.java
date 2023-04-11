package it.polimi.ingsw.model;

import java.util.ArrayList;

public class PersonalGoalCardDeck {
    private final ArrayList<PersonalGoalCard> personalGoalCards;
    public PersonalGoalCardDeck(){
        personalGoalCards = new ArrayList<>();
        personalGoalCards.add(new PersonalGoalCard1());
        personalGoalCards.add(new PersonalGoalCard2());
        personalGoalCards.add(new PersonalGoalCard3());
        personalGoalCards.add(new PersonalGoalCard4());
        personalGoalCards.add(new PersonalGoalCard5());
        personalGoalCards.add(new PersonalGoalCard6());
        personalGoalCards.add(new PersonalGoalCard7());
        personalGoalCards.add(new PersonalGoalCard8());
        personalGoalCards.add(new PersonalGoalCard9());
        personalGoalCards.add(new PersonalGoalCard10());
        personalGoalCards.add(new PersonalGoalCard11());
        personalGoalCards.add(new PersonalGoalCard12());
    }

    /**
     * Draw a random card from the personal goal cards deck
     * @return cardDrawn
     */
    public PersonalGoalCard drawCard(){
        int randomCardDrawnId = (int)(Math.random()*personalGoalCards.size());
        return personalGoalCards.remove(randomCardDrawnId);
    }
}
