package it.polimi.ingsw.model;

import java.util.ArrayList;

public class PersonalGoalCardDeck {
    private ArrayList<PersonalGoalCard> personalGoalCards;
    public static final int NUMBER_OF_PERSONAL_GOAL_CARDS = 12;
    private TileType [][] pattern = new TileType[5][6];


    public PersonalGoalCardDeck(){
        personalGoalCards = new ArrayList<PersonalGoalCard>();

        for(int i = 0; i< NUMBER_OF_PERSONAL_GOAL_CARDS; i++)
            personalGoalCards.add(new PersonalGoalCard(i+1, pattern));

    }

    /**
     * Draw a random card from the personal goal cards deck
     * @return cardDrawn
     */
    public PersonalGoalCard drawCard(){
        int randomCardDrawnId = (int)(Math.random()*personalGoalCards.size());
        PersonalGoalCard cardDrawn = personalGoalCards.get(randomCardDrawnId);
        return  cardDrawn;
    }
}
