package it.polimi.ingsw.util;

import it.polimi.ingsw.model.CommonGoalCard;
import it.polimi.ingsw.model.interfaces.*;

public interface Serializer {
    String serializeGame();
    String serializePlayer(IPlayer player);
    String serializeBoard();
    String serializeBookshelf(IBookshelf bookshelf);
    String serializePersonalGoalCard();
    String serializeCommonGoalCard(CommonGoalCard commonGoalCard);
    String serializeBag();
    String serializeBoardSpace(BoardSpace space);
    String serializeTurn(ITurnManager turnManager);
}
