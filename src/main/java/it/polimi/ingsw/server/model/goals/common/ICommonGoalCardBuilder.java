package it.polimi.ingsw.server.model.goals.common;

import it.polimi.ingsw.util.Builder;

/**
 * Represents a generic builder for CommonGoalCard objects.
 */
public interface ICommonGoalCardBuilder extends Builder<CommonGoalCard> {
    /**
     * Adds points that can be obtained from the card.
     * @param points points that can be obtained.
     * @return the builder's instance.
     */
    ICommonGoalCardBuilder addPoints(int points);

    /**
     * Adds a valid player to the card.
     * @param playerName player that can obtain points from the card.
     * @return the builder's instance.
     */
    ICommonGoalCardBuilder addPlayer(String playerName);
}
