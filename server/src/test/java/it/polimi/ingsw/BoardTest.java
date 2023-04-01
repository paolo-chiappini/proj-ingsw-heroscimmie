package it.polimi.ingsw;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Bookshelf;
import it.polimi.ingsw.model.TileSpace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class BoardTest {
    Board myBoard;
    @BeforeEach
    void setUp(){
        myBoard = new Board();
    }

    /**
     * Test that board needs to be refilled because it's empty
     */
    @Test
    void isBoardEmpty(){
        assertTrue(myBoard.needsRefill());
    }

    /**
     * Test if player can pickUp tiles from tileSpace
     */
    @Test
    void tilesCanBePickedUp(){          //!!!!!!
        int row1 = (int)(Math.random()*6);
        int row2 = (int)(Math.random()*6);
        int col1 = (int)(Math.random()*5);
        int col2 = (int)(Math.random()*5);
        assertFalse(myBoard.canPickUpTiles(row1, col1, row2, col2));
    }

    /**
     * Test that player cannot pick the same tile twice
     */
    @Test
    void pickUp2DifferentTiles(){
        int row1 = (int)(Math.random()* 6);
        int row2 = (int)(Math.random()*6);

        int col1 = (int)(Math.random()*5);
        int col2 = (int)(Math.random()*5);

        List<TileSpace> removedTiles = myBoard.pickUpTiles(row1,col1,row2,col2);

        assertNotEquals(removedTiles.get(0),removedTiles.get(1));

    }
}
