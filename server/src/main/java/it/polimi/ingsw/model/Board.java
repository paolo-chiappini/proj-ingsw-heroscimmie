package it.polimi.ingsw.model;

import it.polimi.ingsw.model.interfaces.GameTile;
import it.polimi.ingsw.util.IBoard;

import java.util.*;

public class Board implements IBoard {
    private static final int BOARD_DIM = 9;
    private TileSpace[][] spaces;

    public Board(){


    }

    /**
     * Check whether the board needs to be refilled or not
     * @return true if board is empty or there's no adjacency among the cards left on the board
     */
    @Override
    public boolean needsRefill(){
        for(int i = 1; i < spaces.length -1; i++){
            for(int j = 1; j < spaces[0].length -1; j++)
                if(spaces[i-1][j] == null && spaces[i][j-1] == null && spaces[i+1][j] == null && spaces[i][j+1] == null)
                    return true;
        }
        return false;
    }

    /**
     *
     * @param bag
     * Tiles are drawn from the bag and put on the board
     */
    @Override
    public void refill(Bag bag){
        for(int i = 0; i < spaces.length; i++)
            for(int j = 0; j < spaces[0].length; j++)
                if(spaces[i][j].canPlaceTile())
                    bag.drawTile();
    }

    /**
     *
     * @param row1
     * @param col1
     * @param row2
     * @param col2
     * @return tiles picked up from the board. They can be one, two or three tiles
     */
    @Override
    public List<TileSpace> pickUpTiles(int row1, int col1, int row2, int col2){
        List<TileSpace> myList = new LinkedList<>();
        int numOfTilesPicked;

        if(row1 == row2 && col1 == col2) {      //player wants to pick a single tile
            myList.add(spaces[row1][col1]);
            spaces[row1][col1].removeTile();
        }

        else if(row1 == row2 && col1 != col2){  //player wants to pick two or three tiles
            if(col2 > col1){
                numOfTilesPicked = col2 - col1 + 1;
                if(numOfTilesPicked == 2 || numOfTilesPicked == 3)
                    for(int i = 0; i < numOfTilesPicked; i++){
                        myList.add(spaces[row1][col1 + i]);
                        spaces[row1][col1 + i].removeTile();
                    }
            }
            else if (col1 > col2) {
                numOfTilesPicked = col1 - col2 + 1;
                if(numOfTilesPicked == 2 || numOfTilesPicked == 3)
                    for (int i = 0; i < numOfTilesPicked; i++) {
                        myList.add(spaces[row1][col2 + i]);
                        spaces[row1][col2 + i].removeTile();
                    }
            }

        }
        else if(row1 != row2 && col1 == col2){
            if(row2 > row1){
                numOfTilesPicked = row2 - row1 + 1;
                if(numOfTilesPicked == 2 || numOfTilesPicked == 3)
                    for(int i = 0; i < numOfTilesPicked; i++){
                        myList.add(spaces[row1+i][col1]);
                        spaces[row1 + i][col1].removeTile();
                    }
            }
            else if (row1 > row2) {
                numOfTilesPicked = row1 - row2 + 1;
                if(numOfTilesPicked == 2 || numOfTilesPicked == 3)
                    for (int i = 0; i < numOfTilesPicked; i++) {
                        myList.add(spaces[row2 + i][col1]);
                        spaces[row2 + i][col1].removeTile();
                    }
            }
        }
        return myList;
    }

    /**
     *
     * @param row1
     * @param col1
     * @param row2
     * @param col2
     * @return true if player can pick up a tile; false if board in position [row1][col1]
     *          or [row2][col2] is empty
     */
    @Override
    public boolean canPickUpTiles(int row1, int col1, int row2, int col2){
        if(spaces[row1][col1] !=null && spaces[row2][col2] != null)
            return true;
        return false;
    }

    @Override
    public void setTileAt(int row, int col, GameTile tile) {

    }

    @Override
    public GameTile getTileAt(int row, int col) {
        return null;
    }

    @Override
    public int getSize() {
        return 0;
    }
}
