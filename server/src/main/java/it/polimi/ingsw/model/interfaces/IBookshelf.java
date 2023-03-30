package it.polimi.ingsw.model.interfaces;

import java.util.List;

public interface IBookshelf {
    default GameTile getTileAt(int row, int column) { return null; }
    default int getWidth() { return 0; }
    default int getHeight() { return 0; }
    default boolean isFull() { return false; }
    void dropTiles(List<GameTile> tilesToDrop, int column);
    boolean canDropTiles(int numOfTiles, int column);
    boolean hasTile(int row, int column);
    boolean compareTiles(int row, int column, int row2, int column2);
}
