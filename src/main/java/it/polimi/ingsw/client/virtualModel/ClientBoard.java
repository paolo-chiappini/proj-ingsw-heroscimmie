package it.polimi.ingsw.client.virtualModel;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * This class represents the board in the virtual model.
 * It aims to represent the state of the board in the client and update it if necessary
 */
public class ClientBoard {

    private int [][] spaces;
    private int boardSize = 9;

    public ClientBoard()
    {
        spaces = new int[boardSize][boardSize];
        for (int i=0;i<spaces.length;i++)
        {
            for(int j=0;j<spaces[0].length;j++)
            {
                spaces[i][j] = -1;
            }
        }
    }

    /**
     * Set a specific tile in a given position
     * @param row is the row of the tile to place
     * @param column is the column of the tile to place
     * @param tileType is the type of the tile to place
     */
    public void setTilesAt(int row, int column, int tileType) {
        spaces[row][column] = tileType;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

    /**
     * Get a specific tile in a given position
     * @param row is the number of row of the tile
     * @param col is the number of column of the tile
     */
    public int getTileAt(int row, int col) {
        return spaces[row][col];
    }

    public int getSize() {
        return boardSize;
    }
    public int[][] getSpaces() {
        return spaces;
    }

    /**
     * Updates tiles in the board
     * @param data contains up-to-date board details
     */
    public void updateBoard(String data)
    {
        JSONObject jsonObject = new JSONObject(data);
        JSONArray board = jsonObject.getJSONArray("board");
        setBoardSize(board.length());
        for(int i=0;i<boardSize;i++)
        {
            for(int j=0; j< board.getJSONArray(i).length();j++)
            {
                setTilesAt(i,j,board.getJSONArray(i).getInt(j));
            }
        }
    }
}