package it.polimi.ingsw.mock;

import it.polimi.ingsw.model.CommonGoalCard;
import it.polimi.ingsw.model.interfaces.IBookshelf;
import it.polimi.ingsw.util.serialization.Serializer;

public class CommonCardMock extends CommonGoalCard {
    public CommonCardMock(int id, int numPlayer) {
        super(id, numPlayer);
    }

    @Override
    public boolean canObtainPoints(IBookshelf bookShelf) {
        return true;
    }

    @Override
    public String serialize(Serializer serializer) {
        return serializer.serialize(this);
    }
}
