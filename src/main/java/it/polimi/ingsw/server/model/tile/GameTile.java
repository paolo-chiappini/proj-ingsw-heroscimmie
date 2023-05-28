package it.polimi.ingsw.server.model.tile;

/**
 * Representation of a generic tile that is associated with a type.
 */
public interface GameTile {
    /**
     * Get the type of tile.
     * @return the type of tile.
     */
    TileType type();
}
