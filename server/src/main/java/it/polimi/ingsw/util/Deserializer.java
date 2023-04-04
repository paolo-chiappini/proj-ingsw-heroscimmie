package it.polimi.ingsw.util;

import it.polimi.ingsw.model.CommonGoalCard;
import it.polimi.ingsw.model.interfaces.*;

/**
 * Represents an object that can deserialize the game model.
 */
public interface Deserializer {
    /**
     * Deserializes the main Game object.
     * Deserialization of game hierarchically deserializes
     * objects contained within.
     * @param data String of data to deserialize into game.
     */
    void deserializeGame(String data);

    /**
     * Deserializes a Player object.
     * Deserialization of player also deserializes objects
     * belonging to player.
     * @param player Player object being deserialized.
     * @param data String of data to deserialize into player.
     */
    void deserializePlayer(IPlayer player, String data);

    /**
     * Deserializes a Board object.
     * @param board Board object being deserialized.
     * @param data String of data to deserialize into board.
     */
    void deserializeBoard(IBoard board, String data);

    /**
     * Deserializes a Bookshelf object.
     * @param bookshelf Bookshelf object being deserialized.
     * @param data String of data to deserialize into bookshelf.
     */
    void deserializeBookshelf(IBookshelf bookshelf, String data);

    /**
     * Deserializes a CommonGoalCard object.
     * @param commonGoalCard Card object being deserialized.
     * @param data String of data to deserialize into card.
     */
    void deserializeCommonGoalCard(CommonGoalCard commonGoalCard, String data);

    /**
     * Deserializes a Bag object.
     * @param bag bag object being deserialized.
     * @param data String of data to deserialize into bag.
     */
    void deserializeBag(IBag bag, String data);

    /**
     * Deserializes a turn into a TurnManger.
     * @param turnManager TurnManager object responsible for managing turns.
     * @param data String of data to deserialize into turn manager.
     */
    void deserializeTurn(ITurnManager turnManager, String data);
}
