package it.polimi.ingsw.server.model.tile;

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
