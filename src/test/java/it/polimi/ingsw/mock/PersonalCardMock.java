package it.polimi.ingsw.mock;

import it.polimi.ingsw.server.model.goals.personal.PersonalGoalCard;
import it.polimi.ingsw.server.model.bookshelf.IBookshelf;

public class PersonalCardMock extends PersonalGoalCard {
    final int points;

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
