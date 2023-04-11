package it.polimi.ingsw.model.interfaces.builders;

import it.polimi.ingsw.model.CommonGoalCard;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.interfaces.ITurnManager;

import java.util.List;

public interface IGameBuilder extends Builder<Game> {
    public IGameBuilder setTurnManager(ITurnManager turnManager);
    public IGameBuilder setCommonGoalCards(List<CommonGoalCard> commonGoals);
}
