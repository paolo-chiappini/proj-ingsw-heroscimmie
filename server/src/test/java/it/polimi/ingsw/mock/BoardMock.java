package it.polimi.ingsw.mock;

import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.model.TileSpace;
import it.polimi.ingsw.model.TileType;
import it.polimi.ingsw.model.interfaces.GameTile;
import it.polimi.ingsw.model.interfaces.IBag;
import it.polimi.ingsw.model.interfaces.IBoard;
import it.polimi.ingsw.util.serialization.Serializer;

import java.util.List;

public class BoardMock implements IBoard {
    int size;
    TileSpace[][] spaces;

    public BoardMock(int size, int[][] template) {
        this.size = size;
        spaces = new TileSpace[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                spaces[i][j] = new TileSpace(0, 0);
                spaces[i][j].setTile(new Tile(TileType.values()[template[i][j]]));
            }
        }
    }

    @Override
    public boolean needsRefill() {
        return false;
    }

    @Override
    public void refill(IBag bag) {

    }

    @Override
    public List<GameTile> pickUpTiles(int row1, int col1, int row2, int col2) {
        return null;
    }

    @Override
    public boolean canPickUpTiles(int row1, int col1, int row2, int col2) {
        return false;
    }

    @Override
    public GameTile getTileAt(int row, int col) {
        return spaces[row][col].getTile();
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public String serialize(Serializer serializer) {
        return serializer.serialize(this);
    }
}
