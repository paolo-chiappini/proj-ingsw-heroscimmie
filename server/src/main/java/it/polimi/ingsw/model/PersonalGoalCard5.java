package it.polimi.ingsw.model;

public class PersonalGoalCard5 extends PersonalGoalCard{
    TileType[][] pattern = new TileType[5][6];
    public PersonalGoalCard5(int id, TileType[][] pattern) {
        super(id, pattern);
        pattern[4][4] = TileType.PLANT;
        pattern[3][1] = TileType.FRAME;
        pattern[5][3] = TileType.CAT;
        pattern[3][2] = TileType.BOOK;
        pattern[5][0] = TileType.TOY;
        pattern[1][1] = TileType.TROPHY;
    }
}
