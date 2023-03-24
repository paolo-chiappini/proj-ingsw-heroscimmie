package it.polimi.ingsw.model;

import it.polimi.ingsw.model.interfaces.GameTile;

public class Tile implements GameTile {
    private final TileType type;

    public Tile(TileType type) {
        this.type = type;
    }

    @Override
    public TileType getType() {
        return type;
    }
}
