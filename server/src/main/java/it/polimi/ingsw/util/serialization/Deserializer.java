package it.polimi.ingsw.util.serialization;

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
     * @param data String of data to deserialize into player.
     * @return instance of the deserialized player.
     */
    IPlayer deserializePlayer(String data);

    /**
     * Deserializes a Board object.
     * @param data String of data to deserialize into board.
     * @return instance of the deserialized board.
     */
    IBoard deserializeBoard(String data);

    /**
     * Deserializes a Bookshelf object.
     * @param data String of data to deserialize into bookshelf.
     * @return instance of the deserialized bookshelf.
     */
    IBookshelf deserializeBookshelf(String data);

    /**
     * Deserializes a CommonGoalCard object.
     * @param data String of data to deserialize into card.
     * @return instance of the deserialized card.
     */
    CommonGoalCard deserializeCommonGoalCard(String data);

    /**
     * Deserializes a Bag object.
     * @param data String of data to deserialize into bag.
     * @return instance of the deserialized bag.
     */
    IBag deserializeBag(String data);

    /**
     * Deserializes a turn into a TurnManger.
     * @param data String of data to deserialize into turn manager.
     * @return instance of a turn manager.
     */
    ITurnManager deserializeTurn(String data);
}
