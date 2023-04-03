package it.polimi.ingsw.util;

import it.polimi.ingsw.model.CommonGoalCard;
import it.polimi.ingsw.model.interfaces.*;

public interface Deserializer {
    void deserializeGame(String data);
    void deserializePlayer(IPlayer player, String data);
    void deserializeBoard(String data);
    void deserializeBookshelf(IBookshelf bookshelf, String data);
    void deserializePersonalGoalCard(String data);
    void deserializeCommonGoalCard(CommonGoalCard commonGoalCard, String data);
    void deserializeBag(String data);
    void deserializeBoardSpace(BoardSpace space, String data);
    void deserializeTurn(ITurnManager turnManager, String data);
}
