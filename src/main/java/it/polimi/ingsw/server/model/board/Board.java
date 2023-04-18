package it.polimi.ingsw.server.model.board;

import it.polimi.ingsw.server.model.tile.GameTile;
import it.polimi.ingsw.server.model.bag.IBag;
import it.polimi.ingsw.server.model.tile.TileType;
import it.polimi.ingsw.util.serialization.Serializer;

import java.util.*;

public class Board implements IBoard {
    private static final int BOARD_DIM = 9;
    private TileSpace[][] spaces;

    public Board(int playersPlaying){
        this.spaces = createSpacesFromTemplate(playersPlaying);
    }

    /**
     * Creates a new instance of Board using a builder.
     * @param builder builder used to create the instance.
     */
    private Board(BoardBuilder builder) {
        this.spaces = builder.spaces;
    }

    /**
     * Creates a new grid of spaces based on the number of players.
     * @param playersPlaying number of players in the game.
     * @return the grid of spaces.
     */
    private static TileSpace[][] createSpacesFromTemplate(int playersPlaying) {
        TileSpace[][] grid = new TileSpace[BOARD_DIM][BOARD_DIM];
        int[][] template = new int[][]{
                {5, 5, 5, 3, 4, 5, 5, 5, 5},
                {5, 5, 5, 2, 2, 4, 5, 5, 5},
                {5, 5, 3, 2, 2, 2, 3, 5, 5},
                {5, 4, 2, 2, 2, 2, 2, 2, 3},
                {4, 2, 2, 2, 2, 2, 2, 2, 4},
                {3, 2, 2, 2, 2, 2, 2, 4, 5},
                {5, 5, 3, 2, 2, 2, 3, 5, 5},
                {5, 5, 5, 4, 2, 2, 5, 5, 5},
                {5, 5, 5, 5, 4, 3, 5, 5, 5}
        };
        for (int i=0;i<grid.length;i++)
        {
            for(int j=0;j<grid[0].length;j++)
            {
                grid[i][j]=new TileSpace(template[i][j],playersPlaying);
            }
        }
        return grid;
    }

    @Override
    public GameTile getTileAt(int row, int col) {
        return spaces[row][col].getTile();
    }

    @Override
    public int getSize() {
        return BOARD_DIM;
    }

    /**
     * Checks whether the board needs to be refilled or not
     * @return true if board is empty or there's no adjacency among the cards left on the board
     */
    @Override
    public boolean needsRefill(){
        for(int i = 0; i < spaces.length; i++) {
            for(int j = 0; j < spaces[0].length; j++) {
                if(spaces[i][j].getTile()!= null) {
                    if ((i == 0) || (j == 0)) {
                        if (spaces[i + 1][j].getTile() != null || spaces[i][j + 1].getTile() != null) return false;
                    } else {
                        if (spaces[i + 1][j].getTile()!= null || spaces[i - 1][j].getTile()!= null || spaces[i][j + 1].getTile()!= null || spaces[i][j - 1].getTile()!= null)
                            return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Tiles are drawn from the bag and put on the board
     * @param bag it is the bag from which the tiles are drawn
     */
    @Override
    public void refill(IBag bag){
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
    @Override
    public List<GameTile> pickUpTiles(int row1, int col1, int row2, int col2){
        List<GameTile> myList = new LinkedList<>();
        int numOfTilesPicked;

        if(row1 == row2 && col1 == col2) {      //player wants to pick a single tile
            myList.add(spaces[row1][col1].getTile());
            spaces[row1][col1].removeTile();
        }

        else if(row1 == row2){  //player wants to pick two or three tiles
            if(col2 > col1){
                numOfTilesPicked = col2 - col1 + 1;
                if(numOfTilesPicked == 2 || numOfTilesPicked == 3)
                    for(int i = 0; i < numOfTilesPicked; i++){
                        myList.add(spaces[row1][col1 + i].getTile());
                        spaces[row1][col1 + i].removeTile();
                    }
            }
            else {
                numOfTilesPicked = Math.abs(col1 - col2) + 1;
                if(numOfTilesPicked == 2 || numOfTilesPicked == 3)
                    for (int i = 0; i < numOfTilesPicked; i++) {
                        myList.add(spaces[row1][col2 + i].getTile());
                        spaces[row1][col2 + i].removeTile();
                    }
            }

        }
        else if(col1 == col2){
            if(row2 > row1){
                numOfTilesPicked = row2 - row1 + 1;
                if(numOfTilesPicked == 2 || numOfTilesPicked == 3)
                    for(int i = 0; i < numOfTilesPicked; i++){
                        myList.add(spaces[row1+i][col1].getTile());
                        spaces[row1 + i][col1].removeTile();
                    }
            }
            else{
                numOfTilesPicked = Math.abs(row1 - row2) + 1;
                if(numOfTilesPicked == 2 || numOfTilesPicked == 3)
                    for (int i = 0; i < numOfTilesPicked; i++) {
                        myList.add(spaces[row2 + i][col1].getTile());
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
    @Override
    public boolean canPickUpTiles(int row1, int col1, int row2, int col2) {
        int temp;
        if(row1==row2)
        {
            if(col2<=col1)
            {
                temp=col1;
                col1=col2;
                col2=temp;
            }
            while(col1<=col2)
            {
                if(spaces[row1][col1].getTile()==null || hasNotFreeSide(row1, col1))return false;
                col1++;
            }
            return true;
        }
        else if(col1==col2)
        {
            if(row2<=row1)
            {
                temp=row1;
                row1=row2;
                row2=temp;
            }
            while(row1<=row2)
            {
                if(spaces[row1][col1].getTile()==null|| hasNotFreeSide(row1, col1))return false;
                row1++;
            }
            return true;
        }
        return false;
    }

    /**
     * @param row1 is the row index of the tile to pick up
     * @param col1 is the column index of the tile to pick up
     * @return false if the tile has at least one side free ;
     */
    public boolean hasNotFreeSide(int row1, int col1)
    {
        if(row1>0 && row1<8 && col1>0 && col1<8)
        {
            return (spaces[row1 + 1][col1].getTile() != null && spaces[row1 - 1][col1].getTile() != null
                    && spaces[row1][col1 + 1].getTile() != null && spaces[row1][col1 - 1].getTile() != null);
        }
        return false;
    }

    @Override
    public String serialize(Serializer serializer) {
        return serializer.serialize(this);
    }

    /**
     * Builder used during the deserialization of a Board object.
     */
    public static class BoardBuilder implements IBoardBuilder {
        private final TileSpace[][] spaces;

        /**
         * @param playersPlaying number of players in the game.
         */
        public BoardBuilder(int playersPlaying) {
            this.spaces = createSpacesFromTemplate(playersPlaying);
        }

        @Override
        public IBoard build() {
            return new Board(this);
        }

        @Override
        public IBoardBuilder setTiles(TileType[][] tileTypes, IBag bag) {
            for (int i = 0; i < tileTypes.length; i++) {
                for (int j = 0; j < tileTypes[i].length; j++) {
                    if (tileTypes[i][j] == null) continue;
                    spaces[i][j].setTile(bag.getTileByType(tileTypes[i][j]));
                }
            }
            return this;
        }
    }
}