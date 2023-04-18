package it.polimi.ingsw.server.model.goals.common;

import it.polimi.ingsw.server.model.bookshelf.IBookshelf;
import it.polimi.ingsw.server.model.player.IPlayer;
import it.polimi.ingsw.util.serialization.Serializable;

public interface GoalCard extends Serializable {
    boolean canObtainPoints(IBookshelf bookShelf);
    int evaluatePoints(IPlayer player);
}
