package it.polimi.ingsw.model.interfaces;

import it.polimi.ingsw.model.TileType;

/**
 * Representation of a generic tile that is associated with a type.
 */
public interface GameTile {
    /**
     * Get the type of tile.
     * @return the type of tile.
     */
    TileType getType();
}
