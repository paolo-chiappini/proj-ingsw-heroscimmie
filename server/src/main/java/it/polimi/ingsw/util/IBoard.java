package it.polimi.ingsw.util;

import it.polimi.ingsw.model.Bag;
import it.polimi.ingsw.model.TileSpace;
import it.polimi.ingsw.model.interfaces.GameTile;
import it.polimi.ingsw.model.interfaces.IBookshelf;

import java.util.List;

public interface IBoard {
    boolean needsRefill();
    void refill(Bag bag);
    List<TileSpace> pickUpTiles(int row1, int col1, int row2, int col2);
    boolean canPickUpTiles(int row1, int col1, int row2, int col2);
    void setTileAt(int row, int col, GameTile tile);
    GameTile getTileAt(int row, int col);
    int getSize();
}
