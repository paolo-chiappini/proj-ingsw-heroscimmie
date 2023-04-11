package it.polimi.ingsw.model.interfaces;

import it.polimi.ingsw.model.PersonalGoalCard;

public interface IPlayer {
    String getUsername();
    int getScore();
    IBookshelf getBookshelf();
    void setBookshelf(IBookshelf bookshelf);
    void setPersonalGoalCard(PersonalGoalCard personalGoalCard);
    PersonalGoalCard getPersonalGoalCard();
    void addPointsToScore(int points);
}
