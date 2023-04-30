package it.polimi.ingsw.server.model.goals.common;

import it.polimi.ingsw.server.model.bookshelf.IBookshelf;

public class CommonGoalCard7 extends CommonGoalCard {
    public CommonGoalCard7(int numPlayer) {
        super(7, numPlayer);
    }

    /**
     * @param bookShelf is the bookshelf on which the achievements of a goal are checked
     * Pattern to achieve:Two groups each containing 4 tiles of the same type in a 2x2 square.
     * The tiles of one square can be different from those of the other square.
     * @return true if the goal is achieved
     */
    @Override
    public boolean canObtainPoints(IBookshelf bookShelf) {
        int countSquare=0;                      //counts the number of total squares
        int countSquareInLine;                  //counts the number of squares in a line (two rows)
        int columnStart=0;                      //indicates the starting column
        int positionOccurrence=0;               //indicates the column in which the left side of the square is located
        int columnEnd=bookShelf.getWidth()-1;   //indicates the last column to be evaluated
        for (int i = 0; i < bookShelf.getHeight()-1; i++)
        {
            countSquareInLine=0;
            for (int j =columnStart; j < columnEnd;j++)
            {
                if(bookShelf.compareTiles(i,j,i,j+1) &&
                        bookShelf.compareTiles(i,j,i+1,j) &&
                            bookShelf.compareTiles(i,j,i+1,j+1))
                {
                    countSquare++;
                    countSquareInLine++;
                    positionOccurrence=j;
                    j++;
                }
            }
            //if it has 2 squares in a line, I go forward 2 rows
            if(countSquareInLine==2)
            {
                i++;
            }
            //if it has 2 squares in a line, I check the position of the square
            else if (countSquareInLine==1)
            {
                //if the square is in the first half of the bookshelf,
                //in the next row I check from the right side of the square to the last column of the bookshelf
                if(positionOccurrence<(bookShelf.getWidth()-2))
                {
                    columnStart=positionOccurrence+2;
                    columnEnd=bookShelf.getWidth()-1;
                }
                //if the square is in the second half of the bookshelf,
                //in the next row I check from the first column of the bookshelf to the left side of the square
                else
                {
                    columnEnd=positionOccurrence;
                    columnStart=0;
                }
            }
            //if it has no squares I check the next row
            else
            {
                columnStart=0;
                columnEnd=bookShelf.getWidth()-1;
            }
        }
        return countSquare>=2;
    }
}
