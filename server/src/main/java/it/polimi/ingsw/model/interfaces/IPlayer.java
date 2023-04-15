package it.polimi.ingsw.model.interfaces;

import it.polimi.ingsw.model.PersonalGoalCard;
import it.polimi.ingsw.util.serialization.Serializable;

/**
 * Represents a generic Player.
 */
public interface IPlayer extends Serializable {
    /**
     * Get the name chosen by the player.
     * @return the username of the player.
     */
    String getUsername();

    /**
     * Get the current score.
     * @return the player's score.
     */
    int getScore();

    /**
     * Get the bookshelf used by the player.
     * @return the bookshelf.
     */
    IBookshelf getBookshelf();

    /**
     * Assigns a bookshelf to the player.
     * @param bookshelf bookshelf to assign to the player.
     */
    void setBookshelf(IBookshelf bookshelf);

    /**
     * Assigns a personal goal card to the player.
     * @param personalGoalCard personal goal card to assign to the player.
     */
    void setPersonalGoalCard(PersonalGoalCard personalGoalCard);

    /**
     * Get the personal goal card assigned to the player.
     * @return the personal goal card.
     */
    PersonalGoalCard getPersonalGoalCard();

    /**
     * Increments the player's score by the specified amount.
     * @param points points to add to the player's score.
     */
    void addPointsToScore(int points);
}
