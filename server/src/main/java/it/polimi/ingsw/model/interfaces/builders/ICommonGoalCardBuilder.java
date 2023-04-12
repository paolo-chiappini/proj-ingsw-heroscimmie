package it.polimi.ingsw.model.interfaces.builders;

import it.polimi.ingsw.model.CommonGoalCard;
import it.polimi.ingsw.model.interfaces.IPlayer;

public interface ICommonGoalCardBuilder extends Builder<CommonGoalCard> {
    ICommonGoalCardBuilder addPoints(int points);
    ICommonGoalCardBuilder addPlayer(IPlayer player);
}
