package it.polimi.ingsw.model.interfaces;

import it.polimi.ingsw.util.serialization.Serializable;

public interface GoalCard extends Serializable {
    boolean canObtainPoints(IBookshelf bookShelf);
    int evaluatePoints(IPlayer player);
}
