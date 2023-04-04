package it.polimi.ingsw.model.interfaces;

import it.polimi.ingsw.model.Player;

public interface GoalCard {
    boolean canObtainPoints(IBookshelf bookShelf);
    int evaluatePoints(Player player);
}
