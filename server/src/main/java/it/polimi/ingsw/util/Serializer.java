package it.polimi.ingsw.util;

import it.polimi.ingsw.model.CommonGoalCard;
import it.polimi.ingsw.model.interfaces.*;

/**
 * Represents an object that can serialize the game model.
 */
public interface Serializer {
    /**
     * Serializes the main Game object.
     * Serialization of game hierarchically serializes game objects
     * contained within.
     * @return serialized String representing Game.
     */
    String serializeGame();

    /**
     * Serializes a Player object.
     * Serialization of player hierarchically serializes objects
     * belonging to player.
     * @param player player to serialize.
     * @return serialized String representing Player.
     */
    String serializePlayer(IPlayer player);

    /**
     * Serializes a Board object.
     * @param board board to serialize.
     * @return serialized String representing Board.
     */
    String serializeBoard(IBoard board);

    /**
     * Serializes a Bookshelf object.
     * @param bookshelf bookshelf to serialize.
     * @return serialized String representing Bookshelf.
     */
    String serializeBookshelf(IBookshelf bookshelf);

    /**
     * Serializes a CommonGoalCard object.
     * @param commonGoalCard common goal card to serialize.
     * @return serialized String representing the card.
     */
    String serializeCommonGoalCard(CommonGoalCard commonGoalCard);

    /**
     * Serializes a Bag object.
     * @param bag bag to serialize.
     * @return serialized String representing Bag.
     */
    String serializeBag(IBag bag);

    /**
     * Serializes a turn from TurnManager object.
     * @param turnManager turn manger containing info regarding the
     *                    turn to serialize.
     * @return serialized String representing a turn.
     */
    String serializeTurn(ITurnManager turnManager);
}
