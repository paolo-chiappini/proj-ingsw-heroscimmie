package it.polimi.ingsw.model.interfaces;

import it.polimi.ingsw.model.PersonalGoalCard;

public interface IPlayer {
    String getUsername();
    int getScore();
    IBookshelf getBookshelf();
    void setPersonalGoalCard(PersonalGoalCard personalGoalCard);
    PersonalGoalCard getPersonalGoalCard();
}
