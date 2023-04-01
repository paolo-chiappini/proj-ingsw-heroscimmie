package it.polimi.ingsw.model;

import java.util.*;

public class Board {
    private TileSpace spaces[][];

    public Board(){
        spaces = new TileSpace[9][9];
    }

    /**
     * Check whether the board needs to be refilled or not
     * @return true if board is empty
     */
    public boolean needsRefill(){
        for(int i = 0; i < spaces.length; i++){
            for(int j = 0; j < spaces[0].length; j++)
                if(spaces[i][j] != null)
                    return false;
        }
        return true;
    }

    /**
     *
     * @param bag
     * Tiles are drawn from the bag and put on the board
     */
    public void refill(Bag bag){
        bag.drawTile();
    }

    /**
     *
     * @param row1
     * @param col1
     * @param row2
     * @param col2
     * @return two tiles picked up from the board
     */
    public List<TileSpace> pickUpTiles(int row1, int col1, int row2, int col2){
        List<TileSpace> myList = new LinkedList<>();
        myList.add(spaces[row1][col1]);
        spaces[row1][col1].removeTile();
        myList.add(spaces[row2][col2]);
        spaces[row2][col2].removeTile();
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
    public boolean canPickUpTiles(int row1, int col1, int row2, int col2){
        if(spaces[row1][col1] !=null && spaces[row2][col2] != null)
            return true;
        return false;
    }
}
