package it.polimi.ingsw.model;

import it.polimi.ingsw.model.interfaces.IBookshelf;

public abstract class PersonalGoalCard {
    private static final int[] pointsTable = new int[]{1,2,4,6,9,12};
    protected TileType[][] pattern = new TileType[6][5];
    private int id;

    public PersonalGoalCard(int id, TileType[][] pattern){
        this.id = id;
        for(int i = 0; i < pattern.length; i++)
            for(int j = 0; j < pattern[0].length; j++)
                this.pattern = pattern;

    }

    /**
     * Evaluate if personal goals are matched with the tiles on the bookshelf
     * if so, the number of points given to player depends on the pointTable
     * @param bookshelf is the player's bookshelf
     * @return pointsAwarded to player
     */
    public int evaluatePoints(IBookshelf bookshelf, TileType[][] pattern){
        int pointsAwarded = 0, matches = 0;     //matches = number of matches between pGoalCard and bookshelf
        for(int i = 0; i < bookshelf.getHeight(); i++)
            for(int j = 0 ; j < bookshelf.getWidth(); j++)
                if(bookshelf.getTileAt(i,j).getType().equals(pattern[i][j]) && bookshelf.getTileAt(i,j) != null)
                    matches++;
        pointsAwarded += pointsTable[matches-1];
        return pointsAwarded;
    }


    public TileType[][] getPattern() {
        return pattern;
    }
}
