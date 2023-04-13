package it.polimi.ingsw.model.interfaces;

import it.polimi.ingsw.model.PersonalGoalCard;
import it.polimi.ingsw.util.serialization.Serializable;

public interface IPlayer extends Serializable {
    String getUsername();
    int getScore();
    IBookshelf getBookshelf();
    void setBookshelf(IBookshelf bookshelf);
    void setPersonalGoalCard(PersonalGoalCard personalGoalCard);
    PersonalGoalCard getPersonalGoalCard();
    void addPointsToScore(int points);
}
