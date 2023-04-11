package it.polimi.ingsw.model;

public class PersonalGoalCard4 extends PersonalGoalCard{
    public PersonalGoalCard4(int id, TileType[][] pattern) {
        super(id, pattern);
        pattern[3][3] = TileType.PLANT;
        pattern[2][2] = TileType.FRAME;
        pattern[4][2] = TileType.CAT;
        pattern[4][1] = TileType.BOOK;
        pattern[0][4] = TileType.TOY;
        pattern[2][0] = TileType.TROPHY;
    }
}
