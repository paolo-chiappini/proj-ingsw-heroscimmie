package it.polimi.ingsw.server.model.goals.personal;

import it.polimi.ingsw.server.model.tile.TileType;

public class PersonalGoalCard8 extends PersonalGoalCard{
    public PersonalGoalCard8() {
        id = 8;
        pattern[3][0] = TileType.PLANT;
        pattern[0][4] = TileType.FRAME;
        pattern[1][1] = TileType.CAT;
        pattern[4][3] = TileType.BOOK;
        pattern[5][3] = TileType.TOY;
        pattern[2][2] = TileType.TROPHY;
    }
}
