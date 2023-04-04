package it.polimi.ingsw.model.interfaces;

public interface GoalCard {
    boolean canObtainPoints(IBookshelf bookShelf);
    int evaluatePoints();
}
