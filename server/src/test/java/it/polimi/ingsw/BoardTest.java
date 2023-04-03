package it.polimi.ingsw;
import it.polimi.ingsw.model.Bag;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Bookshelf;
import it.polimi.ingsw.model.TileSpace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("Board is empty")
    void isBoardEmpty(){
        assertTrue(myBoard.needsRefill());
    }

    /**
     * Test that there's some cards with no adjacency on the board
     * therefore baord needs to be refilled
     */
    @Test
    @DisplayName("Some card has no adjacency on the board")
    void adjacencyAmongCards(){

    }

    /**
     * Test if player can pickUp tiles from tileSpace
     */
    @Test
    @DisplayName("")
    void tilesCanBePickedUp(){
        myBoard.refill(new Bag());
        int row1 = (int)(Math.random()*6);
        int row2 = (int)(Math.random()*6);
        int col1 = (int)(Math.random()*5);
        int col2 = (int)(Math.random()*5);
        assertTrue(myBoard.canPickUpTiles(row1, col1, row2, col2));
    }

    /**
     * Test that player cannot pick the same tile twice
     */
    @Test
    void pickUp2DifferentTiles(){
        myBoard.refill(new Bag());
        int row1 = (int)(Math.random()* 6);
        int row2 = (int)(Math.random()*6);

        int col1 = (int)(Math.random()*5);
        int col2 = (int)(Math.random()*5);

        List<TileSpace> removedTiles = myBoard.pickUpTiles(row1,col1,row2,col2);

        assertNotEquals(removedTiles.get(0),removedTiles.get(1));

    }
}
