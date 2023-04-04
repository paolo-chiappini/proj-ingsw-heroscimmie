package it.polimi.ingsw.model;

public class PersonalGoalCard9 extends PersonalGoalCard{
    TileType[][] pattern = new TileType[5][6];
    public PersonalGoalCard9(int id, TileType[][] pattern) {
        super(id, pattern);
        pattern[4][4] = TileType.PLANT;
        pattern[5][0] = TileType.FRAME;
        pattern[2][2] = TileType.CAT;
        pattern[3][4] = TileType.BOOK;
        pattern[0][2] = TileType.TOY;
        pattern[4][1] = TileType.TROPHY;
    }
}
