package it.polimi.ingsw;
import it.polimi.ingsw.model.Bag;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.interfaces.GameTile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {
    Board myBoard;
    Board myBoard4Player;
    Bag bag;
    @BeforeEach
    void setUp(){
        myBoard = new Board(2);
        myBoard4Player = new Board(4);
        bag=new Bag();
    }

    /**
     * Test that board is 9x9
     */
    @Test
    @DisplayName("Board is 9x9, so size should be 9")
    void boardSize(){
        assertEquals(9,myBoard.getSize());
    }

    /**
     * Test that board needs to be refilled because it's empty
     */
    @Test
    @DisplayName("Board is empty")
    void isBoardEmpty(){
        assertTrue(myBoard.needsRefill());
    }

    /**
     * Test that board doesn't need to be refilled because it's full
     */
    @Test
    @DisplayName("Board is full")
    void isBoardFull(){
        myBoard.refill(bag);
        assertFalse(myBoard.needsRefill());
    }

    /**
     * Test that board doesn't need to be refilled because it's full (4 players)
     */
    @Test
    @DisplayName("Board is full")
    void isBoard4PlayersFull(){
        myBoard4Player.refill(bag);
        assertFalse(myBoard4Player.needsRefill());
    }

    /**
     * Test that board needs to be refilled because there aren't cards with adjacency on the board
     */
    @Test
    @DisplayName("On the board there are only item tiles without any other adjacent tile")
    void noAdjacencyCards(){
        myBoard.refill(bag);
        for(int i = 0; i < 9; i++)
            for(int j = 0; j < 9; j++)
                if(myBoard.canPickUpTiles(i,j,i,j))
                    myBoard.pickUpTiles(i,j,i,j);
        assertTrue(myBoard.needsRefill());
    }

    /**
     * Test that board doesn't need to be refilled because there are some cards with adjacency on the board
     */
    @Test
    @DisplayName("On the board there are some item tiles without any other adjacent tile")
    void AdjacencyCards(){
        myBoard.refill(bag);
        for(int i = 0; i < 9; i++)
            for(int j = 0; j < 9; j++){
                if(myBoard.canPickUpTiles(i,j,i,j))
                    myBoard.pickUpTiles(i,j,i,j);
                i++;
            }
        assertFalse(myBoard.needsRefill());
    }

    /**
     * Test if player can pick up one tile from the board
     * checks that the tile collected is actually the one that was on the board
     */
    @Test
    void tileCanBePickedUp(){
        myBoard.refill(new Bag());
        myBoard.pickUpTiles(6,3,6,3);
        GameTile tileOnBoard = myBoard.getTileAt(5,3);
        assertTrue(myBoard.canPickUpTiles(5,3,5,3));
        List<GameTile> tilesTaken = myBoard.pickUpTiles(5,3,5,3);
        assertAll(
                ()->assertEquals(tileOnBoard,tilesTaken.get(0)),
                ()->assertEquals(1,tilesTaken.size())
        );
    }

    /**
     * Test if player can pick up two tiles in column from the board
     */
    @Test
    void twoTilesColumnCanBePickedUp(){
        myBoard.refill(new Bag());
        assertAll(
                ()->assertTrue(myBoard.canPickUpTiles(7,5,6,5)),
                ()->assertEquals(2,myBoard.pickUpTiles(7,5,6,5).size())
        );
    }

    /**
     * Test if player can pick up two tiles in line from the board
     */
    @Test
    void twoTilesLineCanBePickedUp(){
        myBoard.refill(new Bag());
        assertAll(
                ()->assertTrue(myBoard.canPickUpTiles(7,5,7,4)),
                ()->assertEquals(2,myBoard.pickUpTiles(7,4,7,5).size())
        );
    }

    /**
     * Test if player can pick up three tiles in line from the board
     */
    @Test
    void threeTilesLineCanBePickedUp(){
        myBoard.refill(new Bag());
        myBoard.pickUpTiles(1,3,1,4);
        assertAll(
                ()->assertTrue(myBoard.canPickUpTiles(2,5,2,3)),
                ()->assertEquals(3,myBoard.pickUpTiles(2,5,2,3).size())
        );
    }

    /**
     * Test if player can pick up three tiles in column from the board
     */
    @Test
    void threeTilesColumnCanBePickedUp(){
        myBoard.refill(new Bag());
        myBoard.pickUpTiles(4,7,4,7);
        assertAll(
                ()->assertTrue(myBoard.canPickUpTiles(5,6,3,6)),
                ()->assertEquals(3,myBoard.pickUpTiles(3,6,5,6).size())
        );
    }

    /**
     * Test if player can't pick up three tiles from the board
     */
    @Test
    void threeTilesCannotBePickedUp(){
        myBoard.refill(new Bag());
        assertFalse(myBoard.canPickUpTiles(4,4,4,6));
    }

    /**
     * Test if player can't pick two tiles in diagonal from the board
     */
    @Test
    void diagonalTilesCannotBePickedUp(){
        myBoard.refill(new Bag());
        assertFalse(myBoard.canPickUpTiles(3,1,4,0));
    }

    /**
     * Test that player cannot pick up the same tile twice
     */
    @Test
    void pickUp2SameTiles(){
        myBoard.refill(new Bag());
        assertTrue(myBoard.canPickUpTiles(6,5,7,5));
        myBoard.pickUpTiles(6,5,7,5);
        assertFalse(myBoard.canPickUpTiles(6,5,7,5));
    }

    /**
     * Test that player can pick up the two tiles from spaces of 4 players
     */
    @Test
    void pickUp2Tiles4Players(){
        myBoard4Player.refill(new Bag());
        assertTrue(myBoard4Player.canPickUpTiles(4,8,3,8));
    }
}
