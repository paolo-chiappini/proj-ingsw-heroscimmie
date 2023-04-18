package it.polimi.ingsw.util.serialization;

import it.polimi.ingsw.server.model.board.IBoard;
import it.polimi.ingsw.server.model.bookshelf.IBookshelf;
import it.polimi.ingsw.server.model.goals.common.CommonGoalCard;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.player.IPlayer;
import it.polimi.ingsw.server.model.turn.ITurnManager;

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
    String serialize(Game game);

    /**
     * Serializes a Player object.
     * Serialization of player hierarchically serializes objects
     * belonging to player.
     * @param player player to serialize.
     * @return serialized String representing Player.
     */
    String serialize(IPlayer player);

    /**
     * Serializes a Board object.
     * @param board board to serialize.
     * @return serialized String representing Board.
     */
    String serialize(IBoard board);

    /**
     * Serializes a Bookshelf object.
     * @param bookshelf bookshelf to serialize.
     * @return serialized String representing Bookshelf.
     */
    String serialize(IBookshelf bookshelf);

    /**
     * Serializes a CommonGoalCard object.
     * @param commonGoalCard common goal card to serialize.
     * @return serialized String representing the card.
     */
    String serialize(CommonGoalCard commonGoalCard);

    /**
     * Serializes a turn from TurnManager object.
     * @param turnManager turn manger containing info regarding the
     *                    turn to serialize.
     * @return serialized String representing a turn.
     */
    String serialize(ITurnManager turnManager);
}
