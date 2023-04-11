package it.polimi.ingsw.model;

import java.util.ArrayList;

public class PersonalGoalCardDeck {
    private final ArrayList<PersonalGoalCard> personalGoalCards;
    public PersonalGoalCardDeck(){
        personalGoalCards = new ArrayList<>();

        TileType[][] pattern = new TileType[6][5];
        personalGoalCards.add(new PersonalGoalCard1(1, pattern));
        personalGoalCards.add(new PersonalGoalCard2(2, pattern));
        personalGoalCards.add(new PersonalGoalCard3(3, pattern));
        personalGoalCards.add(new PersonalGoalCard4(4, pattern));
        personalGoalCards.add(new PersonalGoalCard5(5, pattern));
        personalGoalCards.add(new PersonalGoalCard6(6, pattern));
        personalGoalCards.add(new PersonalGoalCard7(7, pattern));
        personalGoalCards.add(new PersonalGoalCard8(8, pattern));
        personalGoalCards.add(new PersonalGoalCard9(9, pattern));
        personalGoalCards.add(new PersonalGoalCard10(10, pattern));
        personalGoalCards.add(new PersonalGoalCard11(11, pattern));
        personalGoalCards.add(new PersonalGoalCard12(12, pattern));
    }

    /**
     * Draw a random card from the personal goal cards deck
     * @return cardDrawn
     */
    public PersonalGoalCard drawCard(){
        int randomCardDrawnId = (int)(Math.random()*personalGoalCards.size());
        return personalGoalCards.get(randomCardDrawnId);
    }
}
