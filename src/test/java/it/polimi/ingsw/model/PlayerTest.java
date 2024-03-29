package it.polimi.ingsw.model;

import it.polimi.ingsw.mock.DynamicTestBookshelf;
import it.polimi.ingsw.server.model.goals.personal.PersonalGoalCard;
import it.polimi.ingsw.server.model.goals.personal.PersonalGoalCard1;
import it.polimi.ingsw.server.model.goals.personal.PersonalGoalCardDeck;
import it.polimi.ingsw.server.model.player.Player;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test on Player class")
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

        @DisplayName("Player should have a bookshelf")
        @Test
        void hasBookshelf() {
            player1.setBookshelf(new DynamicTestBookshelf(new int[][] {{0}}));
            assertNotNull(player1.getBookshelf());
        }

        @Nested
        @DisplayName("Player methods used during the game")
        class PlayerMethods {
            @DisplayName("Assignment of personal goal card")
            @Test
            void isPersonalGoalCardCorrect() {
                PersonalGoalCardDeck deck = new PersonalGoalCardDeck();
                player1.setPersonalGoalCard(deck.drawCard());
                assertNotNull(player1.getPersonalGoalCard());
            }

            @DisplayName("Assignment of a specific personal goal card")
            @Test
            void isPersonalGoalCard1Correct() {
                PersonalGoalCard card = new PersonalGoalCard1();
                player1.setPersonalGoalCard(card);
                assertEquals(card,player1.getPersonalGoalCard());
            }

            @DisplayName("Points are added correctly to the score")
            @Test
            void arePointsAddedToScore() {
                player1.addPointsToScore(8);
                assertEquals(8,player1.getScore());
                player1.addPointsToScore(3);
                assertEquals(11,player1.getScore());
            }
        }
    }
}