package it.polimi.ingsw.model;

public class PersonalGoalCard6 extends PersonalGoalCard{
    TileType[][] pattern = new TileType[5][6];
    public PersonalGoalCard6(int id, TileType[][] pattern) {
        super(id, pattern);
        pattern[5][0] = TileType.PLANT;
        pattern[4][3] = TileType.FRAME;
        pattern[0][4] = TileType.CAT;
        pattern[2][3] = TileType.BOOK;
        pattern[4][1] = TileType.TOY;
        pattern[0][2] = TileType.TROPHY;
    }
}