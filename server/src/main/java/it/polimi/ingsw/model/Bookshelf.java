package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.interfaces.GameTile;
import it.polimi.ingsw.model.interfaces.IBookshelf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Bookshelf implements IBookshelf {
    private final GameTile[][] tiles;
    private static final int WIDTH = 5;
    private static final int HEIGHT = 6;

    public Bookshelf() {
        tiles = new GameTile[HEIGHT][WIDTH];
        for(int i = 0; i< HEIGHT; i++)
        {
            for(int j=0;j<WIDTH;j++)
            {
                tiles[i][j] = null;
            }
        }
    }

    @Override
    public GameTile getTileAt(int row, int column) {
        return tiles[row][column];
    }

    /**
     * @return the width of the bookshelf
     */
    @Override
    public int getWidth() {
        return WIDTH;
    }

    /**
     * @return the height of the bookshelf
     */
    @Override
    public int getHeight() {
        return HEIGHT;
    }

    /**
     * Drops the tiles into the bookshelf
     * @param column the column where you drop the tiles
     * @param tilesToDrop the chosen tiles to insert
    */
    @Override
    public void dropTiles(List<GameTile> tilesToDrop, int column)
    {
        int startRowInsert=0;
        for (int i = 0; i< HEIGHT; i++) {
            if (tiles[i][column]==null)
                startRowInsert=i+1;
        }
        for (GameTile tile : tilesToDrop)
        {
            startRowInsert--;
            tiles[startRowInsert][column] = tile;
        }
    }

    /**
     * checks if the bookshelf has free spaces
     * @param column the column where you want to drop the tiles
     * @param numOfTiles the number of tiles to insert
     * @return true if you can insert the tiles
     */
    @Override
    public boolean canDropTiles(int numOfTiles, int column)
    {
        int AvailableSpaces=0;
        if(numOfTiles>3)
            throw new IllegalActionException("No more than 3 tiles can be inserted, please try again with fewer tiles");
        if(column>WIDTH-1 || column<0)
            throw new IllegalActionException("The chosen column is not valid, try again with another column");
        for(int i = 0; i< HEIGHT; i++)
        {
            if(tiles[i][column]==null)
                AvailableSpaces++;
        }
        return AvailableSpaces >= numOfTiles;
    }

    /**
     * Checks if the bookshelf is full
     * @return true if the bookshelf has free spaces
     */
    @Override
    public boolean isFull()
    {
        return Arrays.stream(tiles).flatMap(Arrays::stream).noneMatch(Objects::isNull);
    }

    /**
     * Checks if the shelf of the bookshelf has a tile
     * @return true if the shelf has a tile
     */
    @Override
    public boolean hasTile(int row, int column)
    {
        return tiles[row][column]!=null;
    }

    /**
     * Compares two tiles
     * @param row is the row in which the first tile is located
     * @param column is the column in which the first tile is located
     * @param row2 is the row of the second tile
     * @param column2 is the column of the second tile
     * @return true if the two tiles are of the same type
     */
    @Override
    public boolean compareTiles(int row, int column, int row2, int column2)
    {
        if (this.hasTile(row,column) && this.hasTile(row2,column2))
        {
            return tiles[row][column].getType().equals(tiles[row2][column2].getType());
        }
        return false;
    }

    /**
     * Chooses the order of the tiles
     * @param tilesToDrop are the chosen tiles to insert
     * @param position1 is the position of the first tile
     * @param position2 is the position of the second tile
     * @param position3 is the position of the third tile
     * @return sorted tiles to insert in the bookshelf
     */
    public List<GameTile> sort(List<GameTile> tilesToDrop, int position1, int position2, int position3)
    {
        List<GameTile> order = new ArrayList<>();
        if(tilesToDrop.size()==3){
            order.add(tilesToDrop.get(position1-1));
            order.add(tilesToDrop.get(position2-1));
            order.add(tilesToDrop.get(position3-1));
        }
       if(tilesToDrop.size()==2) {
            order.add(tilesToDrop.get(position1-1));
            order.add(tilesToDrop.get(position2-1));
        }
        return order;
    }
}