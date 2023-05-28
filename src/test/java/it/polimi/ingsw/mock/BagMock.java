package it.polimi.ingsw.mock;

import it.polimi.ingsw.server.model.tile.Tile;
import it.polimi.ingsw.server.model.tile.TileType;
import it.polimi.ingsw.server.model.tile.GameTile;
import it.polimi.ingsw.server.model.bag.IBag;

import java.util.HashMap;
import java.util.Map;

public class BagMock implements IBag {
    final HashMap<TileType, Integer> tiles;

    public BagMock(Map<TileType, Integer> tiles) {
        this.tiles = new HashMap<>(tiles);
    }

    @Override
    public HashMap<TileType, Integer> getTilesBag() {
        return new HashMap<>(tiles);
    }

    @Override
    public GameTile drawTile() {
        for (Map.Entry<TileType, Integer> entry : tiles.entrySet()) {
            if(entry.getValue() > 0) {
                tiles.put(entry.getKey(), entry.getValue() - 1);
                return new Tile(entry.getKey());
            }
        }
        return null;
    }

    @Override
    public void addTile(GameTile tile) {}

    @Override
    public GameTile getTileByType(TileType type) {
        return new Tile(type);
    }
}
