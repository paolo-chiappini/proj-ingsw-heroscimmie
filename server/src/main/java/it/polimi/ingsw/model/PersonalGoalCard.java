package it.polimi.ingsw.model;

public class PersonalGoalCard{
    private static final int[] pointsTable = new int[]{1,2,4,6,9,12};

    private int id;
    private char[][] pattern;

    public PersonalGoalCard(int id){
        this.id = id;
    }


    /**
     * Evaluate if personal goals are matched with the tiles on the bookshelf
     * if so, the number of points given to player depends on the pointTable
     * @param bookshelf
     * @return pointsAwarded to player
     */
    public int evaluatePoints(Bookshelf bookshelf){
        int pointsAwarded = 0, matches = 0;     //matches = number of matches between pGoalCard and bookshelf
        for(int i = 0; i < bookshelf.getWidth(); i++)
            for(int j = 0 ; i < bookshelf.getHeight(); j++)
                if(bookshelf.getTileAt(i,j).equals(pattern[i][j]) && bookshelf.getTileAt(i,j) != null)
                    matches++;
        pointsAwarded += pointsTable[matches-1];
        return pointsAwarded;
    }
}
