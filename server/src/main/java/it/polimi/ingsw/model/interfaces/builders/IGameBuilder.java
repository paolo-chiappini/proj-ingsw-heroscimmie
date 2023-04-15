package it.polimi.ingsw.model.interfaces.builders;

import it.polimi.ingsw.model.CommonGoalCard;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.interfaces.IBag;
import it.polimi.ingsw.model.interfaces.IBoard;
import it.polimi.ingsw.model.interfaces.ITurnManager;

import java.util.List;

/**
 * Represents a generic builder for Game objects.
 */
public interface IGameBuilder extends Builder<Game> {
    /**
     * Sets the turn manager.
     * @param turnManager turn manager handling the game progression.
     * @return the builder's instance.
     * @throws IllegalArgumentException when trying to set a null turn manager.
     */
     IGameBuilder setTurnManager(ITurnManager turnManager);

    /**
     * Sets the list of common goals.
     * @param commonGoals common goal cards.
     * @return the builder's instance.
     * @throws IllegalArgumentException when common goals aren't exactly 2 or are null.
     */
     IGameBuilder setCommonGoalCards(List<CommonGoalCard> commonGoals);

    /**
     * Sets the bag from which tiles are drawn.
     * @param bag bag of tiles.
     * @return the builder's instance.
     * @throws IllegalArgumentException when trying to set a null bag.
     */
     IGameBuilder setTilesBag(IBag bag);

    /**
     * Sets the board from which tiles are picked.
     * @param board board containing tiles.
     * @return the builder's instance.
     * @throws IllegalArgumentException when trying to set a null board.
     */
     IGameBuilder setBoard(IBoard board);
}
