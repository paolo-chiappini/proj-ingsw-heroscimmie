package it.polimi.ingsw.mock;

import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.model.TileType;
import it.polimi.ingsw.model.interfaces.GameTile;
import it.polimi.ingsw.model.interfaces.IBag;

import java.util.HashMap;
import java.util.Map;

public class BagMock implements IBag {
    HashMap<TileType, Integer> tiles;

    public BagMock(Map<TileType, Integer> tiles) {
        this.tiles = new HashMap<>(tiles);
    }

    @Override
    public HashMap<TileType, Integer> getTilesBag() {
        return new HashMap<>(tiles);
    }

    @Override
    public GameTile drawTile() {
        return null;
    }

    @Override
    public void addTile(GameTile tile) {}

    @Override
    public GameTile getTileByType(TileType type) {
        return new Tile(type);
    }
}
