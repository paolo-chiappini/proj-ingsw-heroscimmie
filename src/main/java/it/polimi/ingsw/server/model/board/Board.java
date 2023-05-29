package it.polimi.ingsw.server.model.board;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.server.model.bag.IBag;
import it.polimi.ingsw.server.model.tile.GameTile;
import it.polimi.ingsw.server.model.tile.TileType;
import it.polimi.ingsw.util.serialization.Serializer;

import java.util.LinkedList;
import java.util.List;

public class Board implements IBoard {
    private static final int BOARD_DIM = 9;
    private final TileSpace[][] spaces;
    // spaces used to check for refill without the need for excessive conditions
    private TileSpace[][] bufferedSpaces;

    public Board(int playersPlaying){
        this.spaces = createSpacesFromTemplate(playersPlaying);
        initTilesBuffer();
    }

    /**
     * Creates a new instance of Board using a builder.
     * @param builder builder used to create the instance.
     */
    private Board(BoardBuilder builder) {
        this.spaces = builder.spaces;
        initTilesBuffer();
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

    private void initTilesBuffer() {
        // init buffered spaces with null tiles
        bufferedSpaces = new TileSpace[BOARD_DIM + 2][BOARD_DIM + 2];
        for (int i = 0; i < bufferedSpaces.length; i++) {
            for (int j = 0; j < bufferedSpaces.length; j++) {
                bufferedSpaces[i][j] = new TileSpace(0, 0);
            }
        }
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
        // copy spaces in buffer
        for (int i = 0; i < spaces.length; i++) {
            System.arraycopy(spaces[i], 0, bufferedSpaces[i + 1], 1, spaces[i].length);
        }

        for (int i = 1; i < bufferedSpaces.length - 1; i++) {
            for (int j = 1; j < bufferedSpaces[i].length - 1; j++) {
                if (bufferedSpaces[i][j].getTile() == null) continue;
                // check for adjacent tiles
                if (bufferedSpaces[i - 1][j].getTile() != null || bufferedSpaces[i + 1][j].getTile() != null ||
                        bufferedSpaces[i][j - 1].getTile() != null || bufferedSpaces[i][j + 1].getTile() != null) return false;
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
                if (bag.getTilesBag().isEmpty()) break;
                else if (space[j].canPlaceTile())
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
        // check if range is valid
        if(!canPickUpTiles(row1, col1, row2, col2)) {
            throw new IllegalActionException("Range of tiles is not valid");
        }

        int startRow, startCol, endRow, endCol;
        List<GameTile> tiles = new LinkedList<>();

        startRow = Integer.min(row1, row2);
        endRow = Integer.max(row1, row2);
        startCol = Integer.min(col1, col2);
        endCol = Integer.max(col1, col2);

        for (int row = startRow; row <= endRow; row++) {
            for (int col = startCol; col <= endCol; col++) {
                tiles.add(spaces[row][col].getTile());
                spaces[row][col].removeTile();
            }
        }

        return tiles;
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
        if (row1 < 0 || row2 < 0 || col1 < 0 || col2 < 0 ||
            row1 >= BOARD_DIM || row2 >= BOARD_DIM || col1 >= BOARD_DIM || col2 >= BOARD_DIM) return false;

        int startRow, startCol, endRow, endCol;
        startRow = Integer.min(row1, row2);
        endRow = Integer.max(row1, row2);
        startCol = Integer.min(col1, col2);
        endCol = Integer.max(col1, col2);

        // check if range is a straight line by ensuring that
        // either row1 = row2 or col1 = col2
        if (row1 != row2 && col1 != col2) return false;
        // check if size of range is valid
        if (endRow - startRow > 2 || endCol - startCol > 2) return false;

        for (int row = startRow; row <= endRow; row++) {
            for (int col = startCol; col <= endCol; col++) {
                if (spaces[row][col].getTile() == null || hasNoFreeSides(row, col)) return false;
            }
        }
        return true;
    }

    /**
     * @param row is the row index of the tile to pick up
     * @param col is the column index of the tile to pick up
     * @return false if the tile has at least one free side;
     */
    public boolean hasNoFreeSides(int row, int col)
    {
        // check if the current space has a tile
        if(spaces[row][col].getTile() == null) {
            throw new IllegalActionException("Cannot check adjacency of empty tile");
        }

        if(row > 0 && row < 8 && col > 0 && col < 8)
        {
            return (spaces[row + 1][col].getTile() != null && spaces[row - 1][col].getTile() != null
                    && spaces[row][col + 1].getTile() != null && spaces[row][col - 1].getTile() != null);
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