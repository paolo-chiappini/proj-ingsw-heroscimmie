package it.polimi.ingsw.model.interfaces;

public interface IBookshelf {
    default GameTile getTileAt(int row, int column) { return null; }
    default int getWidth() { return 0; }
    default int getHeight() { return 0; }
    default boolean isFull() { return false; }
}
