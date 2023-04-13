package it.polimi.ingsw.mock;

import it.polimi.ingsw.model.CommonGoalCard;
import it.polimi.ingsw.model.interfaces.IBookshelf;

public class CommonCardMock extends CommonGoalCard {
    public CommonCardMock(int id, int numPlayer) {
        super(id, numPlayer);
    }

    @Override
    public boolean canObtainPoints(IBookshelf bookShelf) {
        return true;
    }
}
