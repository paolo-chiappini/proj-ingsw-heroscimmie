package it.polimi.ingsw.model;

import java.util.*;

public class Board {
    private static final int BOARD_DIM = 9;
    private TileSpace[][] spaces;

    public Board(){
        spaces = new TileSpace[BOARD_DIM][BOARD_DIM];

    }

    /**
     * Check whether the board needs to be refilled or not
     * @return true if board is empty or there's no adjacency among the cards left on the board
     */
    public boolean needsRefill(){
        for(int i = 0; i < spaces.length; i++) {
            for(int j = 0; j < spaces[0].length; j++) {
                if((i==0) || (j==0)) {
                    if(spaces[i+1][j]!=null || spaces[i][j+1]!=null) return false;
                }
                else if((i==8) || (j==8)) {
                    if(spaces[i-1][j]!=null || spaces[i][j-1]!=null) return false;
                }
                else {
                    if(spaces[i+1][j]!=null || spaces[i-1][j]!=null || spaces[i][j+1]!=null || spaces[i][j-1]!=null) return false;
                }
            }
        }
        return true;
    }

    /**
     *
     * Tiles are drawn from the bag and put on the board
     * @param bag it is the bag from which the tiles are drawn
     */
    public void refill(Bag bag){
        for (TileSpace[] space : spaces)
            for (int j = 0; j < spaces[0].length; j++)
                if (space[j].canPlaceTile())
                    space[j].setTile(bag.drawTile());
    }

    /**
     * @param row1 is the row index of the first tile to pick up
     * @param col1 is the column index of the first tile to pick up
     * @param row2 is the row index of the last tile to pick up
     * @param col2 is the column index of the last tile to pick up
     * @return the list of tiles picked up from the board
     */
    public List<TileSpace> pickUpTiles(int row1, int col1, int row2, int col2){
        List<TileSpace> myList = new LinkedList<>();
        int numOfTilesPicked;

        if(row1 == row2 && col1 == col2) {      //player wants to pick a single tile
            myList.add(spaces[row1][col1]);
            spaces[row1][col1].removeTile();
        }

        else if(row1 == row2){  //player wants to pick two or three tiles
            if(col2 > col1){
                numOfTilesPicked = col2 - col1 + 1;
                if(numOfTilesPicked == 2 || numOfTilesPicked == 3)
                    for(int i = 0; i < numOfTilesPicked; i++){
                        myList.add(spaces[row1][col1 + i]);
                        spaces[row1][col1 + i].removeTile();
                    }
            }
            else {
                numOfTilesPicked = col1 - col2 + 1;
                if(numOfTilesPicked == 2 || numOfTilesPicked == 3)
                    for (int i = 0; i < numOfTilesPicked; i++) {
                        myList.add(spaces[row1][col2 + i]);
                        spaces[row1][col2 + i].removeTile();
                    }
            }

        }
        else if(col1 == col2){
            if(row2 > row1){
                numOfTilesPicked = row2 - row1 + 1;
                if(numOfTilesPicked == 2 || numOfTilesPicked == 3)
                    for(int i = 0; i < numOfTilesPicked; i++){
                        myList.add(spaces[row1+i][col1]);
                        spaces[row1 + i][col1].removeTile();
                    }
            }
            else{
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
     * @param row1 is the row index of the first tile to pick up
     * @param col1 is the column index of the first tile to pick up
     * @param row2 is the row index of the last tile to pick up
     * @param col2 is the column index of the last tile to pick up
     * @return true if player can pick up a tile;
     */
    public boolean canPickUpTiles(int row1, int col1, int row2, int col2) {

        if (row1 == row2) {
            while (col1 <= col2) {
                if (spaces[row1][col1] == null || hasFreeSide(row1, col1)) return false;
                col1++;

            }
        }
        if (col1 == col2) {
            while (row1 <= row2) {
                if (spaces[row1][col1] == null || hasFreeSide(row1, col1)) return false;
                row1++;
            }
        }
        return true;
    }

    /**
     * @param row1 is the row index of the tile to pick up
     * @param col1 is the column index of the tile to pick up
     * @return true if the tile has at least one side free ;
     */
    public boolean hasFreeSide(int row1, int col1)
    {
        if(row1>0 && row1<8 && col1>0 && col1<8)
        {
            return spaces[row1 + 1][col1] != null || spaces[row1 - 1][col1] != null
                    || spaces[row1][col1 + 1] != null || spaces[row1][col1 - 1] != null;
        }
        return false;
    }
}
