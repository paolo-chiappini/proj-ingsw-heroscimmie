package it.polimi.ingsw.mock;

import it.polimi.ingsw.model.TileType;
import it.polimi.ingsw.model.interfaces.GameTile;
import it.polimi.ingsw.model.interfaces.IBookshelf;
import it.polimi.ingsw.util.serialization.Serializer;

import java.util.List;

public class DynamicTestBookshelf implements IBookshelf {
    private final GameTile[][] tiles;

    public DynamicTestBookshelf(int[][] template) {
        tiles = new GameTile[template.length][template[0].length];
        for (int i = 0; i < template.length; i++) {
            for (int j = 0; j < template[i].length; j++) {
                if (template[i][j] < 0) {
                    tiles[i][j] = null;
                    continue;
                }

                TileType type = intToTileType(template[i][j]);
                tiles[i][j] = () -> type;
            }
        }
    }

    private TileType intToTileType(int n) {
        return TileType.values()[n];
    }

    @Override
    public GameTile getTileAt(int row, int column) {
        return tiles[row][column];
    }

    @Override
    public int getWidth() {
        return tiles[0].length;
    }

    @Override
    public int getHeight() {
        return tiles.length;
    }

    @Override
    public boolean isFull() {
        for (int i = 0; i < getHeight(); i++) {
            for (int j = 0; j < getWidth(); j++) {
                if (tiles[i][j] == null) return false;
            }
        }
        return true;
    }

    @Override
    public void dropTiles(List<GameTile> tilesToDrop, int column) {
    }

    @Override
    public boolean canDropTiles(int numOfTiles, int column) {
        return false;
    }

    @Override
    public boolean hasTile(int row, int column) {
        return tiles[row][column]!=null;
    }

    @Override
    public boolean compareTiles(int row, int column, int row2, int column2) {
        if (this.hasTile(row,column) && this.hasTile(row2,column2))
        {
            return tiles[row][column].getType().equals(tiles[row2][column2].getType());
        }
        return false;
    }

    @Override
    public String serialize(Serializer serializer) {
        return serializer.serialize(this);
    }
}
