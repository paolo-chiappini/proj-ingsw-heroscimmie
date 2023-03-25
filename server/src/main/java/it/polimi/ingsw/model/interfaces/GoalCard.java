package it.polimi.ingsw.model.interfaces;

import it.polimi.ingsw.model.Bookshelf;

public interface GoalCard {
    boolean canObtainPoints(Bookshelf bookShelf);
    int evaluatePoints();
}
