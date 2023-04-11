package it.polimi.ingsw.model;

public class PersonalGoalCard7 extends PersonalGoalCard{
    public PersonalGoalCard7(int id, TileType[][] pattern) {
        super(id, pattern);
        pattern[2][1] = TileType.PLANT;
        pattern[1][3] = TileType.FRAME;
        pattern[0][0] = TileType.CAT;
        pattern[5][2] = TileType.BOOK;
        pattern[4][4] = TileType.TOY;
        pattern[3][0] = TileType.TROPHY;
    }
}
