package it.polimi.ingsw;

import it.polimi.ingsw.mock.BoardMock;
import it.polimi.ingsw.model.interfaces.IBoard;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test model serialization")
public class JsonSerializationTest {

    @Test
    @DisplayName("Test board serialization")
    public void testBoardSerialization() {
        IBoard board = new BoardMock(3, new int[][] {
                {1, 2, 3},
                {4, 5, 6},
                {3, 4, 1}
        });

        
    }
}
