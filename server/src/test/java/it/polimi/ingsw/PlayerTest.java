package it.polimi.ingsw;

import it.polimi.ingsw.model.PersonalGoalCard;
import it.polimi.ingsw.model.PersonalGoalCard2;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test on PLayer class")
public class PlayerTest {
    Player player1;

    @Nested
    @DisplayName("On player creation")
    class PlayerCreationTest {
        @BeforeEach
        @DisplayName("Create a new player")
        void setUp() {
            player1 = new Player("TestPlayer");
        }

        @DisplayName("Player's username should be 'TestPlayer'")
        @Test
        void isUsernameCorrect() {
            assertEquals(player1.getUsername(), "TestPlayer");
        }

        @DisplayName("The initial score should be 0")
        @Test
        void isInitialScore0() {
            assertEquals(player1.getScore(), 0);
        }

    }

    @Nested
    @DisplayName("Player methods used during the game")
    class PlayerMethods{
        @DisplayName("Assignment of personal goal card")
        @Test
        void isPersonalGoalCardCorrect(){

        }

        @DisplayName("Points are added correctly to the score")
        @Test
        void arePointsAddedToScore(){

        }
    }
}
