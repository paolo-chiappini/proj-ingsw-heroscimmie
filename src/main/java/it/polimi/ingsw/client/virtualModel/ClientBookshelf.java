package it.polimi.ingsw.client.virtualModel;

import it.polimi.ingsw.util.observer.ModelListener;
import it.polimi.ingsw.util.observer.ObservableObject;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * This class represents a bookshelf in the virtual model.
 * It aims to represent the state of the bookshelf in the client and update it if necessary
 */
public class ClientBookshelf extends ObservableObject<ModelListener> {
    private int [][] tiles;
    private int width = 5;
    private int height = 6;
    public ClientBookshelf() {
        tiles = new int[height][width];
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

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Get a specific tile in a given position
     * @param row is the number of row of the tile
     * @param column is the number of column of the tile
     */
    public int getTileAt(int row, int column) {return tiles[row][column];}

    public int getWidth() {return width;}

    public int getHeight() {return height;}

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
        setHeight(bookshelf.length());
        for(int i=0;i<height;i++)
        {
            setWidth(bookshelf.getJSONArray(i).length());
            for(int j=0; j<width;j++)
            {
                setTilesAt(i,j,bookshelf.getJSONArray(i).getInt(j));
            }
        }
    }
}