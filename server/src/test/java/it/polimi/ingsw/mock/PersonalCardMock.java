package it.polimi.ingsw.mock;

import it.polimi.ingsw.model.PersonalGoalCard;
import it.polimi.ingsw.model.interfaces.IBookshelf;

public class PersonalCardMock extends PersonalGoalCard {
    int points;

    public PersonalCardMock(int id) {
        this.id = id;
        points = 0;
    }

    public PersonalCardMock(int id, int points) {
        this.id = id;
        this.points = points;
    }

    @Override
    public int evaluatePoints(IBookshelf bookshelf) {
        return points;
    }
}
