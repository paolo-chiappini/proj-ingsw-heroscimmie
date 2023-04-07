package it.polimi.ingsw.model.interfaces.builders;

import it.polimi.ingsw.model.CommonGoalCard;
import it.polimi.ingsw.model.interfaces.IPlayer;

public interface ICommonGoalCardBuilder extends Builder<CommonGoalCard> {
    void addPoints(int points);
    void addPlayer(IPlayer player);
}
