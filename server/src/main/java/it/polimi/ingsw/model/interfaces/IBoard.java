package it.polimi.ingsw.model.interfaces;

import it.polimi.ingsw.model.TileSpace;
import it.polimi.ingsw.util.serialization.Serializable;

import java.util.List;

public interface IBoard extends Serializable {
    boolean needsRefill();
    void refill(IBag bag);
    List<GameTile> pickUpTiles(int row1, int col1, int row2, int col2);
    boolean canPickUpTiles(int row1, int col1, int row2, int col2);
    GameTile getTileAt(int row, int col);
    int getSize();
}