package it.polimi.ingsw.util.serialization;

import it.polimi.ingsw.server.model.bag.IBag;
import it.polimi.ingsw.server.model.board.IBoard;
import it.polimi.ingsw.server.model.bookshelf.IBookshelf;
import it.polimi.ingsw.server.model.goals.common.CommonGoalCard;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.player.IPlayer;
import it.polimi.ingsw.server.model.turn.ITurnManager;

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
    Game deserializeGame(String data);

    /**
     * Deserializes a Player object.
     * Deserialization of player also deserializes objects
     * belonging to player.
     * @param data String of data to deserialize into player.
     * @return instance of the deserialized player.
     */
    IPlayer deserializePlayer(String data, IBag bag);

    /**
     * Deserializes a Board object.
     * @param data String of data to deserialize into board.
     * @return instance of the deserialized board.
     */
    IBoard deserializeBoard(String data, int playersCount, IBag bag);

    /**
     * Deserializes a Bookshelf object.
     * @param data String of data to deserialize into bookshelf.
     * @return instance of the deserialized bookshelf.
     */
    IBookshelf deserializeBookshelf(String data, IBag bag);

    /**
     * Deserializes a CommonGoalCard object.
     * @param data String of data to deserialize into card.
     * @return instance of the deserialized card.
     */
    CommonGoalCard deserializeCommonGoalCard(String data);

    /**
     * Deserializes a turn into a TurnManger.
     * @param data String of data to deserialize into turn manager.
     * @return instance of a turn manager.
     */
    ITurnManager deserializeTurn(String data);
}
