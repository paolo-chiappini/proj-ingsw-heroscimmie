package it.polimi.ingsw.model;

import java.util.Arrays;
import java.util.List;

public class Bookshelf {
    private Tile[][] tiles;
    public static final int BOOKSHELF_COLUMN = 5;
    public static final int BOOKSHELF_ROW = 6;

    public Bookshelf() {
        tiles = new Tile[BOOKSHELF_ROW][BOOKSHELF_COLUMN];
        for(int i = 0; i< BOOKSHELF_ROW; i++)
        {
            for(int j=0;j<BOOKSHELF_COLUMN;j++)
            {
                tiles[i][j] = null;
            }
        }
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    /**
     * Drop the tiles into the BookShelf
     * @param column the column where you drop the tiles
     * @param tilesToDrop the chosen tiles to insert
    **/
    public void dropTiles(List<Tile> tilesToDrop, int column)
    {
        int startRowInsert=0;
        for (int i = 0; i< BOOKSHELF_ROW; i++) {
            if (tiles[i][column]==null)
            {
                startRowInsert=i;
            }
        }
        for (Tile tile:tilesToDrop)
        {
            tiles[startRowInsert][column]= new Tile(tile.getType());
            startRowInsert--;
        }
    }

    /**
     * check if the bookshelf has free spaces
     * @param column the column where you want to drop the tiles
     * @param numOfTiles the number of tiles to insert
     * @return true if you can insert the tiles
     **/
    public boolean canDropTiles(int numOfTiles, int column)
    {
        int AvailableSpaces=0;
        for(int i = 0; i< BOOKSHELF_ROW; i++)
        {
            if(tiles[i][column]==null)
                AvailableSpaces++;
        }
        return AvailableSpaces >= numOfTiles;
    }

    /**
     * Check if the bookshelf is full
     * @return true if the bookshelf has free spaces
     **/
    public boolean isFull()
    {
        return Arrays.stream(tiles).flatMap(Arrays::stream).noneMatch(tile -> tile==null);
    }

    /**
     * Check if the shelf of the bookshelf has a tile
     * @return true if the shelf has a tile
     **/
    public boolean hasTile(int row,int column)
    {
        return tiles[row][column]!=null;
    }

    /**
     * Compare two tile
     * @param row is the row in which the first tile is located
     * @param column is the column in which the first tile is located
     * @param row2 is the row of the second tile
     * @param column2 is the column of the second tile
     * @return true if the two tiles are of the same type
     **/
    public boolean compareTiles(int row, int column, int row2 , int column2)
    {
        if (this.hasTile(row,column) && this.hasTile(row2,column2))
        {
            return tiles[row][column].getType().equals(tiles[row2][column2].getType());
        }
        return false;
    }
}