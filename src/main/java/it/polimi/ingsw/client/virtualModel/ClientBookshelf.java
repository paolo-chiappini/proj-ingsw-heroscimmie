package it.polimi.ingsw.client.virtualModel;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * This class represents a bookshelf in the virtual model.
 * It aims to represent the state of the bookshelf in the client and update it if necessary
 */
public class ClientBookshelf {
    private int [][] tiles;
    private static final int WIDTH = 5;
    private static final int HEIGHT = 6;

    public ClientBookshelf() {
        tiles = new int[HEIGHT][WIDTH];
    }

    /**
     * Set a specific tile in a given position
     * @param row is the row of the tile to insert
     * @param column is the column of the tile to insert
     * @param tileType is the type of the tile to insert
     */
    public void setTilesAt(int row, int column, int tileType) {
        tiles[row][column] = tileType;

    }

    /**
     * Get a specific tile in a given position
     * @param row is the number of row of the tile
     * @param column is the number of column of the tile
     */
    public int getTileAt(int row, int column) {return tiles[row][column];}

    public int getWidth() {return WIDTH;}

    public int getHeight() {return HEIGHT;}

    public int[][] getTiles() {
        return tiles;
    }

    /**
     * Updates tiles in the bookshelf
     * @param data contains up-to-date bookshelf details
     */
    public void updateBookshelf(String data)
    {
        JSONObject jsonObject = new JSONObject(data);
        JSONArray bookshelf = jsonObject.getJSONArray("bookshelf");
        for(int i=0;i<bookshelf.length();i++)
        {
            for(int j=0; j<bookshelf.getJSONArray(i).length();j++)
            {
                setTilesAt(i,j,bookshelf.getJSONArray(i).getInt(j));
            }
        }
    }
}