package it.polimi.ingsw.server.model.goals.personal;

import it.polimi.ingsw.server.model.tile.TileType;

public class PersonalGoalCard2 extends PersonalGoalCard{

    public PersonalGoalCard2() {
        id = 2;
        pattern[1][1] = TileType.PLANT;
        pattern[5][4] = TileType.FRAME;
        pattern[2][0] = TileType.CAT;
        pattern[3][4] = TileType.BOOK;
        pattern[2][2] = TileType.TOY;
        pattern[4][3] = TileType.TROPHY;
    }
}
