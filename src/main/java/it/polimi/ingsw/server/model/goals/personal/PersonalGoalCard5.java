package it.polimi.ingsw.server.model.goals.personal;

import it.polimi.ingsw.server.model.tile.TileType;

public class PersonalGoalCard5 extends PersonalGoalCard{
    public PersonalGoalCard5() {
        id = 5;
        pattern[4][4] = TileType.PLANT;
        pattern[3][1] = TileType.FRAME;
        pattern[5][3] = TileType.CAT;
        pattern[3][2] = TileType.BOOK;
        pattern[5][0] = TileType.TOY;
        pattern[1][1] = TileType.TROPHY;
    }
}
