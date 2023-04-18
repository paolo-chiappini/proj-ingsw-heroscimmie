package it.polimi.ingsw.server.model.bag;

import it.polimi.ingsw.server.model.tile.GameTile;
import it.polimi.ingsw.server.model.tile.TileType;

import java.util.HashMap;

/**
 * Represents a generic Bag object.
 */
public interface IBag {
    /**
     * Get the map that represents how many tiles are left for each type.
     * @return the map of tiles.
     */
    HashMap<TileType, Integer> getTilesBag();

    /**
     * Randomly draws a tile from the available set of remaining tiles.
     * @return the drawn tile.
     */
    GameTile drawTile();

    /**
     * Adds an instance of tile to the bag.
     * @param tile tile to add to the bag.
     */
    void addTile(GameTile tile);

    /**
     * Returns a tile by the specified type.
     * @param type type of tile to draw.
     * @return the drawn tile.
     */
    GameTile getTileByType(TileType type);
}
