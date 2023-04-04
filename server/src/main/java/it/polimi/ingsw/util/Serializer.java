package it.polimi.ingsw.util;

import it.polimi.ingsw.model.CommonGoalCard;
import it.polimi.ingsw.model.interfaces.*;

public interface Serializer {
    String serializeGame();
    String serializePlayer(IPlayer player);
    String serializeBoard(IBoard board);
    String serializeBookshelf(IBookshelf bookshelf);
    String serializePersonalGoalCard();
    String serializeCommonGoalCard(CommonGoalCard commonGoalCard);
    String serializeBag();
    String serializeTurn(ITurnManager turnManager);
}
