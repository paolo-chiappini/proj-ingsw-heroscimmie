package it.polimi.ingsw.model;

public class PersonalGoalCard{
    private static final int[] pointsTable = new int[]{1,2,4,6,9,12};

    private int id;
    private String[][] pattern;

    public PersonalGoalCard(int id){
        this.id = id;
        pattern = new String[5][6];

            switch (id){
                case 1 -> {
                    pattern[0][0] = "P";
                    pattern[0][2] = "F";
                    pattern[1][4] = "C";
                    pattern[2][3] = "B";
                    pattern[3][1] = "G";
                    pattern[5][2] = "T";
                    break;
                }

                case 2 -> {
                    pattern[1][1] = "P";
                    pattern[4][5] = "F";
                    pattern[2][0] = "C";
                    pattern[3][5] = "B";
                    pattern[2][2] = "G";
                    pattern[4][3] = "T";
                    break;
                }

                case 3 -> {
                    pattern[2][2] = "P";
                    pattern[1][0] = "F";
                    pattern[3][1] = "C";
                    pattern[5][0] = "B";
                    pattern[1][3] = "G";
                    pattern[3][4] = "T";
                    break;
                }

                case 4 -> {
                    pattern[3][3] = "P";
                    pattern[2][2] = "F";
                    pattern[4][2] = "C";
                    pattern[4][1] = "B";
                    pattern[0][4] = "G";
                    pattern[2][0] = "T";
                    break;
                }

                case 5 -> {
                    pattern[4][4] = "P";
                    pattern[3][1] = "F";
                    pattern[5][3] = "C";
                    pattern[3][2] = "B";
                    pattern[5][0] = "G";
                    pattern[1][1] = "T";
                    break;
                }

                case 6 -> {
                    pattern[5][0] = "P";
                    pattern[4][3] = "F";
                    pattern[0][4] = "C";
                    pattern[2][3] = "B";
                    pattern[4][1] = "G";
                    pattern[0][2] = "T";
                    break;
                }

                case 7 -> {
                    pattern[2][1] = "P";
                    pattern[1][3] = "F";
                    pattern[0][0] = "C";
                    pattern[5][2] = "B";
                    pattern[4][4] = "G";
                    pattern[3][0] = "T";
                    break;
                }

                case 8 -> {
                    pattern[3][0] = "P";
                    pattern[0][4] = "F";
                    pattern[1][1] = "C";
                    pattern[4][3] = "B";
                    pattern[5][3] = "G";
                    pattern[2][2] = "T";
                    break;
                }

                case 9 -> {
                    pattern[4][4] = "P";
                    pattern[5][0] = "F";
                    pattern[2][2] = "C";
                    pattern[3][4] = "B";
                    pattern[0][2] = "G";
                    pattern[4][1] = "T";
                    break;
                }

                case 10 -> {
                    pattern[5][3] = "P";
                    pattern[4][1] = "F";
                    pattern[3][3] = "C";
                    pattern[2][0] = "B";
                    pattern[1][1] = "G";
                    pattern[0][4] = "T";
                    break;
                }

                case 11 -> {
                    pattern[0][2] = "P";
                    pattern[3][2] = "F";
                    pattern[4][4] = "C";
                    pattern[1][1] = "B";
                    pattern[2][0] = "G";
                    pattern[5][3] = "T";
                    break;
                }

                case 12 -> {
                    pattern[1][1] = "P";
                    pattern[2][2] = "F";
                    pattern[5][0] = "C";
                    pattern[0][2] = "B";
                    pattern[4][4] = "G";
                    pattern[3][3] = "T";
                    break;
                }

            }

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
